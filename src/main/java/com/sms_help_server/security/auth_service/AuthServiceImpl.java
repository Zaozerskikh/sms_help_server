package com.sms_help_server.security.auth_service;

import com.sms_help_server.entities.base.EntityStatus;
import com.sms_help_server.entities.password_reset_token.PasswordResetToken;
import com.sms_help_server.entities.role.RoleName;
import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.repo.PasswordResetTokenRepository;
import com.sms_help_server.repo.RoleRepository;
import com.sms_help_server.repo.UserRepository;
import com.sms_help_server.security.exceptions.RegistrationException;
import com.sms_help_server.security.jwt.JwtTokenProvider;
import com.sms_help_server.services.email_service.EmailService;
import com.sms_help_server.services.user_service.PasswordChangeException;
import com.sms_help_server.services.user_service.PasswordResetException;
import com.sms_help_server.services.user_service.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Override
    public String authentificate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SmsHelpUser user = userService.findByEmail(email);
        return jwtTokenProvider.createJwtToken(email, user.getRoles());
    }

    @Override
    public SmsHelpUser register(SmsHelpUser user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(List.of(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow()));
            user.setEntityStatus(EntityStatus.ACTIVE);
            SmsHelpUser registeredUser = userRepository.save(user);
            emailService.sendSuccessfullRegistrationMessage(registeredUser);
            return registeredUser;
        } catch (Exception e) {
            throw new RegistrationException("Registration failed, contact support. Stack trace\n" + Arrays.toString(e.getStackTrace()));
        }
    }

    @SneakyThrows
    @Override
    public SmsHelpUser updateUserPassword(SmsHelpUser user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new PasswordChangeException("old password equals to a new password.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public String generatePasswordResetToken(SmsHelpUser user, String linkContext) {
        String token = UUID.randomUUID().toString();
        if (user.getToken() != null) {
            user.getToken().setToken(token);
            passwordResetTokenRepository.save(user.getToken());
        } else {
            passwordResetTokenRepository.save(new PasswordResetToken(user, token));
        }

        String link = linkContext + "/api/v1/auth/resetPassword/" + token;
        emailService.sendPasswordResetLinkMessage(user, link);
        System.out.println(link);
        return token;
    }

    @Override
    @SneakyThrows
    public SmsHelpUser resetUserPassword(String passwordResetToken, String newPassword) {
        PasswordResetToken token = passwordResetTokenRepository.findByToken(passwordResetToken);

        if (token == null ) {
            throw new PasswordResetException("incorrect password reset link");
        }

        if (token.getExpirationDate().before(new Date())) {
            throw new PasswordResetException("password reset link was expired");
        }

        SmsHelpUser user =  this.updateUserPassword(token.getUser(), newPassword);
        emailService.sendSuccessfullPasswordResetMessage(user);
        return user;
    }
}

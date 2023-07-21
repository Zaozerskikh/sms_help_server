package com.sms_help_server.security.auth_service;

import com.sms_help_server.entities.base.EntityStatus;
import com.sms_help_server.entities.role.RoleName;
import com.sms_help_server.entities.tokens.password_reset_token.PasswordResetToken;
import com.sms_help_server.entities.tokens.verification_token.VerificationToken;
import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.repo.PasswordResetTokenRepository;
import com.sms_help_server.repo.RoleRepository;
import com.sms_help_server.repo.UserRepository;
import com.sms_help_server.repo.VerificationTokenRepository;
import com.sms_help_server.security.exceptions.RegistrationException;
import com.sms_help_server.security.exceptions.VerificationException;
import com.sms_help_server.security.jwt.JwtTokenProvider;
import com.sms_help_server.services.email_service.EmailService;
import com.sms_help_server.services.user_service.PasswordChangeException;
import com.sms_help_server.services.user_service.PasswordResetException;
import com.sms_help_server.services.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Value("${frontend.host}")
    private String frontendHost;

    @Value("${frontend.verification.link.prefix}")
    private String frontendVerificationLinkPrefix;

    @Value("${frontend.password_reset.link.prefix}")
    private String frontendPasswordResetLinkPrefix;

    @Override
    public String authentificate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SmsHelpUser user = userService.findByEmail(email);
        return jwtTokenProvider.createJwtToken(email, user.getRoles());
    }

    @Override
    public SmsHelpUser register(String nickname, String email, String password) {
        try {
            SmsHelpUser user = new SmsHelpUser(nickname, email, passwordEncoder.encode(password));
            user.setRoles(List.of(roleRepository.findByName(RoleName.ROLE_USER).orElseThrow()));
            user.setEntityStatus(EntityStatus.ACTIVE);
            SmsHelpUser registeredUser = userRepository.saveAndFlush(user);

            String verificationToken = this.generateNewVerificationToken(registeredUser);
            String verificationLink = frontendHost + frontendVerificationLinkPrefix + verificationToken;
            emailService.sendVerificationMessage(registeredUser, verificationLink);

            return registeredUser;
        } catch (Exception e) {
            throw new RegistrationException(e.getMessage());
        }
    }

    @Override
    public SmsHelpUser updateUserPassword(SmsHelpUser user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new PasswordChangeException("old password equals to a new password.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        return userRepository.saveAndFlush(user);
    }

    @Override
    public String generatePasswordResetToken(SmsHelpUser user) {
        try {
            String token = UUID.randomUUID().toString();
            if (user.getPasswordResetToken() != null) {
                user.getPasswordResetToken().setTokenValue(token);
                passwordResetTokenRepository.save(user.getPasswordResetToken());
            } else {
                passwordResetTokenRepository.save(new PasswordResetToken(user, token));
            }

            String link = frontendHost + frontendPasswordResetLinkPrefix + token;
            emailService.sendPasswordResetLinkMessage(user, link);
            return token;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    @Override
    public SmsHelpUser resetUserPassword(String passwordResetToken, String newPassword) {
        PasswordResetToken token = this.findAndCheckPasswordResetToken(passwordResetToken);
        SmsHelpUser user =  this.updateUserPassword(token.getUser(), newPassword);
        emailService.sendSuccessfullPasswordResetMessage(user);
        passwordResetTokenRepository.delete(token);
        return user;
    }

    @Override
    public PasswordResetToken findAndCheckPasswordResetToken(String passwordResetToken) {
        PasswordResetToken token = passwordResetTokenRepository
                .findByTokenValue(passwordResetToken)
                .orElseThrow(() -> new PasswordResetException("incorrect password reset link"));

        if (token.getExpirationDate().before(new Date())) {
            throw new PasswordResetException("password reset link was expired");
        }

        return token;
    }

    @Override
    public SmsHelpUser verifyUser(String verificationToken) {
        VerificationToken token = verificationTokenRepository
                .findByTokenValue(verificationToken)
                .orElseThrow(() -> new VerificationException(HttpStatus.BAD_REQUEST, "incorrect verification link"));

        if (token.getExpirationDate().before(new Date())) {
            throw new VerificationException(HttpStatus.BAD_REQUEST, "verification link was expired");
        }

        SmsHelpUser user = token.getUser();
        user.setIsVerified(true);

        SmsHelpUser verifiedUser = userRepository.saveAndFlush(user);
        try {
            verificationTokenRepository.deleteById(token.getVerificationTokenId());
        } catch (ObjectOptimisticLockingFailureException ignored) { }

        return verifiedUser;
    }

    @Override
    public String generateNewVerificationToken(SmsHelpUser user) {
        if (user.getIsVerified()) {
            throw new VerificationException(HttpStatus.BAD_REQUEST, "already verified");
        }

        String tokenValue = UUID.randomUUID().toString();
        if (user.getVerificationToken() != null) {
            user.getVerificationToken().setTokenValue(tokenValue);
            verificationTokenRepository.save(user.getVerificationToken());
        } else {
            verificationTokenRepository.save(new VerificationToken(user, tokenValue));
        }

        return tokenValue;
    }
}

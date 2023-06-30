package com.sms_help_server.security.auth_controller;

import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.security.auth_service.AuthService;
import com.sms_help_server.security.dto.JwtResponseDTO;
import com.sms_help_server.security.dto.LoginRequestDTO;
import com.sms_help_server.security.dto.NewPasswordDTO;
import com.sms_help_server.security.dto.RegistrationRequestDTO;
import com.sms_help_server.security.exceptions.JwtAuthentificationException;
import com.sms_help_server.security.exceptions.RegistrationException;
import com.sms_help_server.services.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto) {
        try {
            return ResponseEntity.ok(new JwtResponseDTO(authService.authentificate(
                    loginRequestDto.getEmail(),
                    loginRequestDto.getPassword()
            )));
        } catch (Exception e) {
            throw new JwtAuthentificationException(e.getMessage());
        }
    }

    @PutMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(@RequestBody RegistrationRequestDTO registrationRequestDto) {
        try {
            SmsHelpUser user = userService.findByEmail(registrationRequestDto.getEmail());
            throw new RegistrationException("User already registered");
        } catch (UsernameNotFoundException e) {
            String email = registrationRequestDto.getEmail();
            String password = registrationRequestDto.getPassword();
            authService.register(new SmsHelpUser(
                    registrationRequestDto.getNickname(),
                    email,
                    password
            ));
            return ResponseEntity.ok(new JwtResponseDTO(authService.authentificate(email, password)));
        }
    }

    @GetMapping("/getResetPasswordToken")
    public ResponseEntity<String> getResetPasswordToken(
            @RequestParam(required = true) String email) {
        SmsHelpUser user = userService.findByEmail(email);
        authService.generatePasswordResetToken(user, "http://localhost:8080");
        return ResponseEntity.ok("Password reset link was sent to your email address.");
    }

    @PatchMapping("/resetPassword/{token}")
    public ResponseEntity<String> resetPassword(
            @PathVariable String token,
            @RequestBody NewPasswordDTO newPasswordDTO) {
        try {
            authService.resetUserPassword(token, newPasswordDTO.getNewPassword());
            return ResponseEntity.ok("Your password has been successfully updated");
        } catch (Exception e) {
            throw new JwtAuthentificationException(e.getMessage());
        }
    }
}
package com.sms_help_server.security.auth_controller;

import com.sms_help_server.controllers.BaseResponseDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
            String token = authService.authentificate(
                    loginRequestDto.getEmail(),
                    loginRequestDto.getPassword()
            );

            Long userId = userService.findByEmail(loginRequestDto.getEmail()).getUserId();
            return ResponseEntity.ok(new JwtResponseDTO(token, userId));
        } catch (Exception e) {
            throw new JwtAuthentificationException(e.getMessage());
        }
    }

    @PutMapping("/register")
    public ResponseEntity<JwtResponseDTO> register(@Valid @RequestBody RegistrationRequestDTO registrationRequestDto) {
        try {
            SmsHelpUser user = userService.findByEmail(registrationRequestDto.getEmail());
            throw new RegistrationException("User already registered");
        } catch (UsernameNotFoundException e) {
            String nickname = registrationRequestDto.getNickname();
            String email = registrationRequestDto.getEmail();
            String password = registrationRequestDto.getPassword();
            Long userId = authService.register(nickname, email, password).getUserId();
            return ResponseEntity.ok(new JwtResponseDTO(authService.authentificate(email, password), userId));
        }
    }

    @GetMapping("/getResetPasswordToken")
    public ResponseEntity<BaseResponseDTO> getResetPasswordToken(
            @RequestParam(required = true) String email) {
        SmsHelpUser user = userService.findByEmail(email);
        authService.generatePasswordResetToken(user);
        return ResponseEntity.ok(new BaseResponseDTO(
                HttpStatus.OK.value(),
                "Password reset link was sent to your email address."
        ));
    }

    @PatchMapping("/resetPassword/{token}")
    public ResponseEntity<BaseResponseDTO> resetPassword(
            @PathVariable String token,
            @Valid @RequestBody NewPasswordDTO newPasswordDTO) {
        authService.resetUserPassword(token, newPasswordDTO.getNewPassword());
        return ResponseEntity.ok(new BaseResponseDTO(
                HttpStatus.OK.value(),
                "Your password has been successfully updated"
        ));
    }

    @GetMapping("/checkResetPasswordToken/{token}")
    public ResponseEntity<BaseResponseDTO> checkResetPasswordToken(@PathVariable String token) {
        authService.findAndCheckPasswordResetToken(token);
        return ResponseEntity.ok(new BaseResponseDTO(
                HttpStatus.OK.value(),
                "token is valid"
        ));
    }

    @PatchMapping("/verify/{token}")
    public ResponseEntity<BaseResponseDTO> verifyAccount(@PathVariable String token) {
        authService.verifyUser(token);
        return ResponseEntity.ok(new BaseResponseDTO(
                HttpStatus.OK.value(),
                "Your password has been successfully updated"
        ));
    }
}

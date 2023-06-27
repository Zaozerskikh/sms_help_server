package com.sms_help_server.security.controllers;

import com.sms_help_server.entities.user.SmsHelpUser;
import com.sms_help_server.security.dto.JwtResponseDTO;
import com.sms_help_server.security.dto.LoginRequestDTO;
import com.sms_help_server.security.dto.RegistrationRequestDTO;
import com.sms_help_server.security.exceptions.JwtAuthentificationException;
import com.sms_help_server.security.exceptions.RegistrationException;
import com.sms_help_server.security.jwt.JwtTokenProvider;
import com.sms_help_server.services.user_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto) {
        try {
            return ResponseEntity.ok(authentificate(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
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
            userService.register(new SmsHelpUser(
                    registrationRequestDto.getNickname(),
                    email,
                    password
            ));
            return ResponseEntity.ok(authentificate(email, password));
        }
    }

    private JwtResponseDTO authentificate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SmsHelpUser user = userService.findByEmail(email);
        String token = jwtTokenProvider.createJwtToken(email, user.getRoles());
        return new JwtResponseDTO(token);
    }
}

package com.sms_help_server.security.controllers;

import com.sms_help_server.security.exceptions.JwtAuthentificationException;
import com.sms_help_server.security.exceptions.RegistrationException;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log
@RestControllerAdvice
public class AuthAdviceController {
    @ExceptionHandler(JwtAuthentificationException.class)
    public ResponseEntity<String> handleJwtAuthentificationException(JwtAuthentificationException exception) {
        log.info("JWT AUTHENTIFICATION EXCEPTION: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(exception.getMessage());
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(RegistrationException exception) {
        log.info("REGISTRATION EXCEPTION: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        log.info("USERNAME NOT FOUND: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}

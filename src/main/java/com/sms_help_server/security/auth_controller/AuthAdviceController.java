package com.sms_help_server.security.auth_controller;

import com.sms_help_server.controllers.advice.dto.HttpExceptionDTO;
import com.sms_help_server.security.exceptions.JwtAuthentificationException;
import com.sms_help_server.security.exceptions.RegistrationException;
import lombok.extern.java.Log;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log
@Order(1)
@RestControllerAdvice
public class AuthAdviceController {
    @ExceptionHandler(JwtAuthentificationException.class)
    public ResponseEntity<HttpExceptionDTO> handleJwtAuthentificationException(JwtAuthentificationException exception) {
        log.info("JWT AUTHENTIFICATION EXCEPTION: " + exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new HttpExceptionDTO(
                        HttpStatus.FORBIDDEN.value(),
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<HttpExceptionDTO> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        log.info("USERNAME NOT FOUND: " + exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new HttpExceptionDTO(HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage()
                ));
    }
}

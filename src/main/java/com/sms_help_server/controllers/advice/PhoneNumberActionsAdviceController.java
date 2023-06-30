package com.sms_help_server.controllers.advice;

import com.sms_help_server.services.phone_number_service.NumberRegistrationException;
import lombok.extern.java.Log;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log
@Order(1)
@RestControllerAdvice
public class PhoneNumberActionsAdviceController {
    @ExceptionHandler(NumberRegistrationException.class)
    public ResponseEntity<String> handleRegistrationException(NumberRegistrationException exception) {
        log.info("NUMBER REGISTRATION EXCEPTION: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}

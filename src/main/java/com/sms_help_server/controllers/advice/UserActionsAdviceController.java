package com.sms_help_server.controllers.advice;

import com.sms_help_server.controllers.advice.dto.HttpExceptionDTO;
import com.sms_help_server.services.user_service.PasswordChangeException;
import lombok.extern.java.Log;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@Log
@RestControllerAdvice
@Order(1)
public class UserActionsAdviceController {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<HttpExceptionDTO> handleNoSuchElementException(NoSuchElementException exception) {
        log.info("NO SUCH ELEMENT EXCEPTION: " + exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new HttpExceptionDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(PasswordChangeException.class)
    public ResponseEntity<HttpExceptionDTO> handlePasswordChangeException(PasswordChangeException exception) {
        log.info("PASSWORD CHANGE EXCEPTION: " + exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new HttpExceptionDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage()
                ));
    }
}
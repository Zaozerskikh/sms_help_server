package com.sms_help_server.controllers.advice;

import com.sms_help_server.controllers.advice.dto.HttpExceptionDTO;
import com.sms_help_server.services.email_service.EmailException;
import lombok.extern.java.Log;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Log
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalAdviceController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpExceptionDTO> handleException(Exception exception) {
        log.info("INTERNAL SERVER ERROR: " + exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new HttpExceptionDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<HttpExceptionDTO> handleHttpException(HttpException exception) {
        log.info("HTTP " + exception.getStatus().value() + " : " + exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new HttpExceptionDTO(exception));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleException(HttpMessageNotReadableException exception) {
        log.info("HTTP MESSAGE NOT READABLE: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<String> handleEmailException(EmailException exception) {
        log.info("EMAIL EXCEPTION: " + exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}

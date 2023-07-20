package com.sms_help_server.controllers.advice;

import com.sms_help_server.controllers.advice.dto.HttpExceptionDTO;
import com.sms_help_server.controllers.advice.dto.ValidationExceptionDTO;
import com.sms_help_server.services.email_service.EmailException;
import lombok.extern.java.Log;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalAdviceController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpExceptionDTO> handleException(Exception exception) {
        log.info("INTERNAL SERVER ERROR: " + exception.getClass() + "  :  " + exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new HttpExceptionDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        exception.getClass() + ":" + exception.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationExceptionDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        Map<String, String> errors = result
                .getAllErrors()
                .stream()
                .collect(Collectors.toMap(
                        error -> ((FieldError) error).getField(),
                        error -> Optional.ofNullable(error.getDefaultMessage()).orElse("")
                ));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ValidationExceptionDTO(
                        HttpStatus.BAD_REQUEST.value(), "validation error", result.getObjectName(), errors
                ));
    }

    @ExceptionHandler(HttpException.class)
    public ResponseEntity<HttpExceptionDTO> handleHttpException(HttpException exception) {
        return ResponseEntity
                .status(exception.getStatus())
                .body(new HttpExceptionDTO(exception));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HttpExceptionDTO> handleException(HttpMessageNotReadableException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new HttpExceptionDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<HttpExceptionDTO> handleEmailException(EmailException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new HttpExceptionDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpExceptionDTO> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new HttpExceptionDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        exception.getMessage()
                ));
    }
}

package com.sms_help_server.security.exceptions;

import com.sms_help_server.controllers.advice.HttpException;
import org.springframework.http.HttpStatus;

public class RegistrationException extends HttpException {
    public RegistrationException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}

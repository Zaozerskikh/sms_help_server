package com.sms_help_server.services.user_service;

import com.sms_help_server.controllers.advice.HttpException;
import org.springframework.http.HttpStatus;

public class PasswordResetException extends HttpException {
    public PasswordResetException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}

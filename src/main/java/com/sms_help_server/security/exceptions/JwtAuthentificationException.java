package com.sms_help_server.security.exceptions;

import com.sms_help_server.controllers.advice.HttpException;
import org.springframework.http.HttpStatus;

public class JwtAuthentificationException extends HttpException {
    public JwtAuthentificationException(String message) {
        super(HttpStatus.UNAUTHORIZED, message);
    }
}

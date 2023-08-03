package com.sms_help_server.security.exceptions;

import com.sms_help_server.controllers.advice.HttpException;
import org.springframework.http.HttpStatus;

public class TransactionException extends HttpException {
    public TransactionException(HttpStatus status, String message) {
        super(status, message);
    }
}

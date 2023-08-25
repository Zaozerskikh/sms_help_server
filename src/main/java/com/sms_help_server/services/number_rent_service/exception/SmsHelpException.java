package com.sms_help_server.services.number_rent_service.exception;

import com.sms_help_server.controllers.advice.HttpException;
import org.springframework.http.HttpStatus;

public class SmsHelpException extends HttpException {
    public SmsHelpException(HttpStatus status, String message) {
        super(status, message);
    }
}
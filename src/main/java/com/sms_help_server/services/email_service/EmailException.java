package com.sms_help_server.services.email_service;

public class EmailException extends RuntimeException {
    public EmailException(String message) {
        super(message);
    }
}

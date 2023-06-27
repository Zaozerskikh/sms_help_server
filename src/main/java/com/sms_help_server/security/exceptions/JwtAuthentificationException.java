package com.sms_help_server.security.exceptions;

import org.springframework.security.core.AuthenticationException;

public class JwtAuthentificationException extends AuthenticationException {
    public JwtAuthentificationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtAuthentificationException(String msg) {
        super(msg);
    }

    public JwtAuthentificationException() {
        super("403");
    }
}

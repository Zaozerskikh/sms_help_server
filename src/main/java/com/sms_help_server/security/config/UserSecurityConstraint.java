package com.sms_help_server.security.config;

import com.sms_help_server.security.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("userSecurity")
public class UserSecurityConstraint {
    public boolean validateUserId(Authentication authentication, Long userId) {
        try {
            return ((JwtUser)authentication.getPrincipal()).getId().equals(userId);
        } catch (Exception e) {
            return false;
        }
    }
}

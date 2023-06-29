package com.sms_help_server.security.config;

import com.sms_help_server.security.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component("userSecurity")
public class UserSecurityConstraint {
    public boolean validateUserId(Authentication authentication, HttpServletRequest request) {
        try {
            return ((JwtUser)authentication.getPrincipal()).getId().equals(Long.parseLong(request.getParameter("userId")));
        } catch (Exception e) {
            return false;
        }
    }
}

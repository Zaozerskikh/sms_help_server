package com.sms_help_server.security.config;

import com.sms_help_server.security.jwt.JwtUser;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component("userSecurity")
public class UserSecurityConstraint {
    public boolean valudateUser(Authentication authentication, HttpServletRequest request) {
        JwtUser jwtUser =  (JwtUser)authentication.getPrincipal();
        Object rawUserId = request.getParameter("userId");
        String userEmail = request.getParameter("email");

        if (rawUserId == null && userEmail == null) {
            return false;
        }

        if (userEmail == null) {
            try {
                return jwtUser.getId().equals(Long.parseLong(rawUserId.toString()));
            } catch (Exception e) {
                return false;
            }
        } else {
            return jwtUser.getEmail().equals(userEmail);
        }
    }
}

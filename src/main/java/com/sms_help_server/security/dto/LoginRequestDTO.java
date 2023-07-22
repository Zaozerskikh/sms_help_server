package com.sms_help_server.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    private String email;
    private String password;

    public void setEmail(String email) {
        this.email = email.toLowerCase();
    }
}
package com.sms_help_server.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationRequestDTO {
    private String nickname;
    private String email;
    private String password;
}

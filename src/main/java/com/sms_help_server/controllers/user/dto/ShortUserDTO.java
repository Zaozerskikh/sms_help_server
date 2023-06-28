package com.sms_help_server.controllers.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortUserDTO {
    private Long id;
    private String email;
    private String nickname;
}

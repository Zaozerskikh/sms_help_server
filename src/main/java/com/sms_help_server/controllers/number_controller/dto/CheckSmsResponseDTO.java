package com.sms_help_server.controllers.number_controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckSmsResponseDTO {
    private Boolean isRecieved;
    private String code;
}

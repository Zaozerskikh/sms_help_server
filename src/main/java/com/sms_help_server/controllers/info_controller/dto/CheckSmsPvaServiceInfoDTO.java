package com.sms_help_server.controllers.info_controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckSmsPvaServiceInfoDTO {
    private String serviceCode;
    private Double price;
    private Double multiplier;
    private boolean isAvailable;
}


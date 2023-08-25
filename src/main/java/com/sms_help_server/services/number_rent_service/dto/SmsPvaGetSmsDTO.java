package com.sms_help_server.services.number_rent_service.dto;

import lombok.Data;

@Data
public class SmsPvaGetSmsDTO {
    private String number;
    private int response;
    private String sms;
}

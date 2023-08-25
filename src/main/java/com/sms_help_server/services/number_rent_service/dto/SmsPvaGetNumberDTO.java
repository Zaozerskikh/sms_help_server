package com.sms_help_server.services.number_rent_service.dto;

import lombok.Data;

@Data
public class SmsPvaGetNumberDTO {
    private int response;
    private String number;
    private String id;
    private String country;
    private String countryCode;
}
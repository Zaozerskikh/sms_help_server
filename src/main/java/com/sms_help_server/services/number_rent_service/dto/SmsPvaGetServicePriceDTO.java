package com.sms_help_server.services.number_rent_service.dto;

import lombok.Data;

@Data
public class SmsPvaGetServicePriceDTO {
    private int response;
    private String country;
    private String service;
    private double price;
}

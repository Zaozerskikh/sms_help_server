package com.sms_help_server.services.number_rent_service.dto;

import lombok.Data;

@Data
public class SmsPvaCheckServiceStockDTO {
    private String service;
    private Long online;
    private String country;
}
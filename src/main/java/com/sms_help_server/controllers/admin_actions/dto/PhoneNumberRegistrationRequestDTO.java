package com.sms_help_server.controllers.admin_actions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PhoneNumberRegistrationRequestDTO {
    private String number;
    private String country;
    private List<Long> serviceIds;
}

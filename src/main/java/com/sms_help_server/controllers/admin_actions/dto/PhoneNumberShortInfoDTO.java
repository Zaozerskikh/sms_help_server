package com.sms_help_server.controllers.admin_actions.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberShortInfoDTO {
    private Long numberId;
    private String number;
    private String country;
}

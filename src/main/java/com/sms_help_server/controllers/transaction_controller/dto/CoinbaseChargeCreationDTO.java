package com.sms_help_server.controllers.transaction_controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CoinbaseChargeCreationDTO {
    private String chargeId;
    private Long userId;
    private Double amount;
    private String description;
}

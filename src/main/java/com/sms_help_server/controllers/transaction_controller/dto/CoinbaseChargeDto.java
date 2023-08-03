package com.sms_help_server.controllers.transaction_controller.dto;

import com.sms_help_server.entities.transaction.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CoinbaseChargeDto {
    private String chargeId;
    private Long userId;
    private TransactionStatus status;
    private String description;
    private Double amount;
}

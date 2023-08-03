package com.sms_help_server.controllers.transaction_controller.dto;

import com.sms_help_server.entities.transaction.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CoinbaseChargeStatusDTO {
    private String chargeId;
    private TransactionStatus transactionStatus;
}

package com.sms_help_server.services.transaction_service;

import com.sms_help_server.entities.transaction.TransactionStatus;
import com.sms_help_server.entities.transaction.coinbase_top_up.CoinbaseCharge;

public interface TransactionService {
    CoinbaseCharge createNewCharge(String chargeId, Long userId, Double amount, String description);

    TransactionStatus refreshCharge(String chargeId);
}

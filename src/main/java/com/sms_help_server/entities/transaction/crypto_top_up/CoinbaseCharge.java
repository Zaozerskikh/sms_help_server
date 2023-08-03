package com.sms_help_server.entities.transaction.crypto_top_up;

import com.sms_help_server.entities.transaction.Transaction;
import com.sms_help_server.entities.user.SmsHelpUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity(name = "coinbase_charge")
@EqualsAndHashCode(callSuper = true)
public class CoinbaseCharge extends Transaction {
    @Id
    @Column(name = "coinbase_charge_id")
    private String coinbaseChargeId;

    public CoinbaseCharge(String chargeId, SmsHelpUser user, String description, Double amount) {
        this.amount = amount;
        this.user = user;
        this.coinbaseChargeId = chargeId;
        this.descriptiton = description;
    }
}
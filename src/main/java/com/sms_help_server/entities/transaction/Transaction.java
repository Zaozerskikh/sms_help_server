package com.sms_help_server.entities.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.user.SmsHelpUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public class Transaction extends BaseEntity {
    @Column(name = "description")
    private String descriptiton;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "transaction_status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus transactionStatus;

    @JsonIgnoreProperties({"topUps", "numberPurchases"})
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SmsHelpUser user;

    public Transaction(Double amount, String descriptiton) {
        this.amount = amount;
        this.descriptiton = descriptiton;
    }

    public Transaction(Double amount) {
        this.amount = amount;
        this.descriptiton = "no description provided";
    }
}

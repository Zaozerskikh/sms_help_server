package com.sms_help_server.entities.transaction.purchase;

import com.sms_help_server.entities.phone_number.PhoneNumber;
import com.sms_help_server.entities.service.Service;
import com.sms_help_server.entities.transaction.Transaction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "number_purchase")
@EqualsAndHashCode(callSuper = true)
public class NumberPurchase extends Transaction {
    @Id
    @Column(name = "purchase_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long purchaseId;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "number_id")
    private PhoneNumber number;

    public NumberPurchase(Double amount, String descriptiton, Service service, PhoneNumber number) {
        super(amount, descriptiton);
        this.service = service;
        this.number = number;
    }

    public NumberPurchase(Double amount, Service service, PhoneNumber number) {
        super(amount);
        this.service = service;
        this.number = number;
    }
}

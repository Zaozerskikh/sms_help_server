package com.sms_help_server.entities.transaction.purchase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sms_help_server.entities.rent_fact.RentFact;
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
    @Column(name = "number_purchase_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long numberPurchaseId;

    @JsonIgnoreProperties("numberPurchases")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @OneToOne(mappedBy = "numberPurchase", fetch = FetchType.LAZY)
    private RentFact rentFact;
}

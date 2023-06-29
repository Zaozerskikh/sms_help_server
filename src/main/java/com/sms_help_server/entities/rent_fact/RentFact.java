package com.sms_help_server.entities.rent_fact;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.phone_number.PhoneNumber;
import com.sms_help_server.entities.transaction.purchase.NumberPurchase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity(name = "rent_fact")
@EqualsAndHashCode(callSuper = true)
public class RentFact extends BaseEntity {
    @Id
    @Column(name = "rent_fact_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rentFactId;

    @Column(name = "status", nullable = false)
    private RentFactStatus status;

    @Column(name = "rent_started")
    private Date rentStarted;

    @Column(name = "rent_ended")
    private Date rentEnded;

    @JsonIgnoreProperties("numberPurchases")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "number_id")
    private PhoneNumber number;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "number_purchase_id")
    private NumberPurchase numberPurchase;
}

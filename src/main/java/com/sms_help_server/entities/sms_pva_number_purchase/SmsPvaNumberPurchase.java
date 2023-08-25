package com.sms_help_server.entities.sms_pva_number_purchase;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.user.user_entity.SmsHelpUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Entity(name = "sms_pva_number_purchase")
public class SmsPvaNumberPurchase extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "purchase_id")
    private Long purchaseId;

    @Column(name = "sms_pva_purchase_id")
    private String smsPvaPurchaseId;

    @Column(name = "number")
    private String number;

    @Column(name = "country")
    private  String country;

    @Column(name = "country_code")
    private Integer countryCode;

    @Column(name = "service_code")
    private String serviceCode;

    @Column(name = "sms_pva_price")
    private Double smsPvaPrice;

    @Column(name = "user_price")
    private Double userPrice;

    @Column(name = "rent_started")
    private Date rentStarted;

    @Column(name = "rent_ended")
    private Date rentEnded;

    @Column(name = "code")
    private String code;

    @JsonIgnoreProperties("smsPvaNumberPurchases")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    protected SmsHelpUser user;

    public SmsPvaNumberPurchase(SmsHelpUser user, String smsPvaPurchaseId, String serviceCode,
                                String number, String country, Integer countryCode,
                                Double smsPvaPrice, Double userPrice, int expirationInMinutes) {
        this.user = user;
        this.smsPvaPurchaseId = smsPvaPurchaseId;
        this.number = number;
        this.smsPvaPrice = smsPvaPrice;
        this.userPrice = userPrice;
        this.countryCode = countryCode;
        this.serviceCode = serviceCode;
        this.country = country;

        rentStarted = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(rentStarted);
        calendar.add(Calendar.MINUTE, expirationInMinutes);
        rentEnded = calendar.getTime();
    }
}

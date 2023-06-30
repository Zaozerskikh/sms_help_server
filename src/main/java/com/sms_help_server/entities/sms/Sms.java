package com.sms_help_server.entities.sms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.rent_fact.RentFact;
import com.sms_help_server.entities.service.Service;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "sms")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Sms extends BaseEntity {
    @Id
    @Column(name = "sms_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long smsId;

    @JsonIgnoreProperties("recievedSms")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id")
    private Service service;

    @Column(name = "code")
    private String code;

    @JsonIgnoreProperties("sms")
    @OneToOne(mappedBy = "sms")
    private RentFact rentFact;
}

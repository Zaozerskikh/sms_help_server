package com.sms_help_server.entities.phone_number;

import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.rent_fact.RentFact;
import com.sms_help_server.entities.service.Service;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "phone_number")
@EqualsAndHashCode(callSuper = true)
public class PhoneNumber extends BaseEntity {
    @Id
    @Column(name = "number_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long numberId;

    @Column(name = "number")
    private String number;

    @Column(name = "country")
    private String country;

    @ManyToMany(mappedBy = "availableNumbers", fetch = FetchType.LAZY)
    private List<Service> availableServices;

    @OneToMany(mappedBy = "number", fetch = FetchType.LAZY)
    private List<RentFact> rentFacts;

    public PhoneNumber(String number, String country) {
        this.number = number;
        this.country = country;
    }
}


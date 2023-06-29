package com.sms_help_server.entities.phone_number;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.rent_fact.RentFact;
import com.sms_help_server.entities.service_number_relation.ServiceNumberRelation;
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

    @Column(name = "number", nullable = false, unique = true, updatable = false)
    private String number;

    @Column(name = "country", nullable = false, updatable = false)
    private String country;

    @JsonIgnoreProperties({"phoneNumber", "service"})
    @OneToMany(
            mappedBy = "phoneNumber",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}
    )
    private List<ServiceNumberRelation> serviceNumberRelations;

    @JsonIgnoreProperties("number")
    @OneToMany(
            mappedBy = "number",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}
    )
    private List<RentFact> rentFacts;

    public PhoneNumber(String number, String country) {
        this.number = number;
        this.country = country;
    }
}


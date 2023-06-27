package com.sms_help_server.entities.service;

import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.phone_number.PhoneNumber;

import javax.persistence.*;
import java.util.List;

@Entity(name = "service")
public class Service extends BaseEntity {
    @Id
    @Column(name = "service_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long serviceId;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Double price;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "services_numbers",
            joinColumns = {@JoinColumn(name = "service_id", referencedColumnName = "service_id")},
            inverseJoinColumns = {@JoinColumn(name = "number_id", referencedColumnName = "number_id")}
    )
    private List<PhoneNumber> availableNumbers;
}

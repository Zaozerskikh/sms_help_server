package com.sms_help_server.entities.phone_number;

import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.service.Service;

import javax.persistence.*;
import java.util.List;

@Entity(name = "phone_number")
public class PhoneNumber extends BaseEntity {
    @Id
    @Column(name = "number_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long numberId;

    @Column(name = "number")
    private String number;

    @ManyToMany(mappedBy = "availableNumbers", fetch = FetchType.LAZY)
    private List<Service> availableServices;
}


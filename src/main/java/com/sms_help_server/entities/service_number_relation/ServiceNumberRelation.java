package com.sms_help_server.entities.service_number_relation;

import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.phone_number.PhoneNumber;
import com.sms_help_server.entities.service.Service;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "service_number_relation")
@EqualsAndHashCode(callSuper = true)
public class ServiceNumberRelation extends BaseEntity {
    @EmbeddedId
    private ServiceNumberRelationId id = new ServiceNumberRelationId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("serviceId")
    private Service service;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("numberId")
    private PhoneNumber phoneNumber;

    @Column(name = "relation_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ServiceNumberRelationStatus relationStatus;

    public ServiceNumberRelation(Service service, PhoneNumber phoneNumber) {
        this.service = service;
        this.phoneNumber = phoneNumber;
        this.relationStatus = ServiceNumberRelationStatus.UNUSED;
    }

    public ServiceNumberRelation(Service service, PhoneNumber phoneNumber, ServiceNumberRelationStatus status) {
        this.service = service;
        this.phoneNumber = phoneNumber;
        this.relationStatus = status;
    }
}


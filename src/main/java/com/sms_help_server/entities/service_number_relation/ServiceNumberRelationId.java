package com.sms_help_server.entities.service_number_relation;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
public class ServiceNumberRelationId implements Serializable {
    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "number_id")
    private Long numberId;
}

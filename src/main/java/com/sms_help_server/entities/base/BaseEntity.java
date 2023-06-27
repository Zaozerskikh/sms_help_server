package com.sms_help_server.entities.base;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @CreatedDate
    @Column(name = "created", nullable = false, updatable = false)
    protected Date createdDate;

    @LastModifiedDate
    @Column(name = "updated")
    protected Date updatedDate;

    @Column(name = "entity_status", columnDefinition = "varchar(32) default 'ACTIVE'")
    @Enumerated(EnumType.STRING)
    protected EntityStatus entityStatus = EntityStatus.ACTIVE;
}

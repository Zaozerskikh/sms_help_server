package com.sms_help_server.entities.base;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public class BaseEntity {
    @CreatedDate
    @Column(name = "created")
    protected Date createdDate;

    @LastModifiedDate
    @Column(name = "updated")
    protected Date updatedDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    protected Status status;
}

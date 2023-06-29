package com.sms_help_server.entities.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.service_number_relation.ServiceNumberRelation;
import com.sms_help_server.entities.transaction.purchase.NumberPurchase;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "service")
@EqualsAndHashCode(callSuper = true)
public class Service extends BaseEntity {
    @Id
    @Column(name = "service_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long serviceId;

    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Column(name = "price", nullable = false)
    private Double price;

    @JsonIgnoreProperties("service")
    @OneToMany(
            mappedBy = "service",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}
    )
    private List<ServiceNumberRelation> serviceNumberRelations;

    @JsonIgnoreProperties("service")
    @OneToMany(
            mappedBy = "service",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}
    )
    private List<NumberPurchase> numberPurchases;

    public Service(String serviceName, Double price) {
        this.name = serviceName;
        this.price = price;
    }
}

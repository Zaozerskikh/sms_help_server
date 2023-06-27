package com.sms_help_server.entities.service;

import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.phone_number.PhoneNumber;
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

    @OneToMany(mappedBy = "service", fetch = FetchType.LAZY)
    private List<NumberPurchase> numberPurchases;

    public Service(String serviceName, Double price) {
        this.name = serviceName;
        this.price = price;
    }
}

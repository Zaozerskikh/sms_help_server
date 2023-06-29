package com.sms_help_server.entities.transaction.top_up;

import com.sms_help_server.entities.transaction.Transaction;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "top_up")
@EqualsAndHashCode(callSuper = true)
public class TopUp extends Transaction {
    @Id
    @Column(name = "top_up_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long topUpId;

    @Column(name = "type", nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private TopUpType type;
}

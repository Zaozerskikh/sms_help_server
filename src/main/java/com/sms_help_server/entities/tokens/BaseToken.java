package com.sms_help_server.entities.tokens;

import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.user.SmsHelpUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class BaseToken extends BaseEntity {
    protected static final Long EXPIRATION = 604800000L;

    @Column(name = "token", nullable = false, updatable = true, unique = true)
    protected String tokenValue;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    protected SmsHelpUser user;

    @Column(name = "expiration_date", updatable = true)
    protected Date expirationDate;

    @PrePersist
    @PreUpdate
    void setExpirationDate() {
        expirationDate = new Date(createdDate.getTime() + EXPIRATION);
    }

    public BaseToken(SmsHelpUser user, String tokenValue) {
        this.user = user;
        this.tokenValue = tokenValue;
    }
}

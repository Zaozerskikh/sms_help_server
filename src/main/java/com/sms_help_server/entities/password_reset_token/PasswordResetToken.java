package com.sms_help_server.entities.password_reset_token;

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
@Entity(name = "password_reset_token")
@EqualsAndHashCode(callSuper = true)
@EntityListeners(AuditingEntityListener.class)
public class PasswordResetToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "password_reset_token_id")
    private Long passwordResetTokenId;

    @Column(name = "token", nullable = false, updatable = true, unique = true)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private SmsHelpUser user;

    @Column(name = "expiration_date", updatable = true)
    private Date expirationDate;

    @PrePersist
    @PreUpdate
    void setExpirationDate() {
        expirationDate = new Date(createdDate.getTime() + 604800000);
    }

    public PasswordResetToken(SmsHelpUser user, String token) {
        this.user = user;
        this.token = token;
    }
}

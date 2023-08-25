package com.sms_help_server.entities.tokens.password_reset_token;

import com.sms_help_server.entities.tokens.BaseToken;
import com.sms_help_server.entities.user.user_entity.SmsHelpUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "password_reset_token")
@EqualsAndHashCode(callSuper = true)
public class PasswordResetToken extends BaseToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "password_reset_token_id")
    private Long passwordResetTokenId;

    public PasswordResetToken(SmsHelpUser user, String tokenValue) {
        super(user, tokenValue);
    }
}

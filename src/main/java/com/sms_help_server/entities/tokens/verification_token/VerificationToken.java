package com.sms_help_server.entities.tokens.verification_token;

import com.sms_help_server.entities.tokens.BaseToken;
import com.sms_help_server.entities.user.SmsHelpUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity(name = "verification_token")
@EqualsAndHashCode(callSuper = true)
public class VerificationToken extends BaseToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "verification_token_id")
    private Long verificationTokenId;

    public VerificationToken(SmsHelpUser user, String tokenValue) {
        super(user, tokenValue);
    }
}

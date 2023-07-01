package com.sms_help_server.entities.user_password;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.user.SmsHelpUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity(name = "user_password")
@EqualsAndHashCode(callSuper = true)
public class UserPassword extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_password_id")
    private Long userPasswordId;

    @Column(name = "password")
    private String password;

    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties("passwordHistory")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private SmsHelpUser user;

    public UserPassword(SmsHelpUser user, String password) {
        this.user = user;
        this.password = password;
    }
}

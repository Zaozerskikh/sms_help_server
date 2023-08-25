package com.sms_help_server.entities.user.user_entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.role.Role;
import com.sms_help_server.entities.sms_pva_number_purchase.SmsPvaNumberPurchase;
import com.sms_help_server.entities.tokens.password_reset_token.PasswordResetToken;
import com.sms_help_server.entities.tokens.verification_token.VerificationToken;
import com.sms_help_server.entities.transaction.coinbase_top_up.CoinbaseCharge;
import com.sms_help_server.entities.user.user_password.UserPassword;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.*;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity(name = "sms_help_user")
public class SmsHelpUser extends BaseEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long userId;

    @Size(min = 3, max = 20)
    @Column(name = "nickname", unique = true)
    private String nickname;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "balance")
    private double balance = 0;

    @Column(name = "is_verified", columnDefinition = "boolean default false")
    private Boolean isVerified;

    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set<UserPassword> passwordHistory;

    @JsonIgnoreProperties("users")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "role_id")}
    )
    private List<Role> roles;

    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<CoinbaseCharge> coibaseTopUps;

    @JsonIgnoreProperties("user")
    @OneToMany(mappedBy = "user",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            orphanRemoval = true
    )
    private List<SmsPvaNumberPurchase> smsPvaNumberPurchases;

    @JsonIgnoreProperties("user")
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private PasswordResetToken passwordResetToken;

    @JsonIgnoreProperties("user")
    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private VerificationToken verificationToken;

    public void setPassword(String password) {
        passwordHistory.add(new UserPassword(this, password));
    }

    public String getPassword() {
        return this.passwordHistory
                .stream()
                .sorted(Comparator.comparing(BaseEntity::getCreatedDate).reversed())
                .toList()
                .get(0)
                .getPassword();
    }

    public SmsHelpUser(String username, String email, String password) {
        this.passwordHistory = new HashSet<>();
        this.coibaseTopUps = new ArrayList<>();
        this.smsPvaNumberPurchases = new ArrayList<>();
        this.roles = new ArrayList<>();

        this.nickname = username;
        this.passwordHistory.add(new UserPassword(this, password));
        this.email = email;
        this.isVerified = false;
    }

    public double increaseBalance(double amount) {
        this.balance += amount;
        return this.balance;
    }
}
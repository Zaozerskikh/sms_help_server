package com.sms_help_server.entities.role;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sms_help_server.entities.base.BaseEntity;
import com.sms_help_server.entities.user.SmsHelpUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "roles")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseEntity {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long roleId;

    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    @JsonIgnoreProperties("roles")
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<SmsHelpUser> users;

    public Role(RoleName roleName) {
        this.name = roleName;
    }
}

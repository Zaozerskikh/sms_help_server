package com.sms_help_server.entities.role;

import lombok.Getter;

@Getter
public enum RoleName {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN");

    private final String value;

    RoleName(String value) {
        this.value = value;
    }
}

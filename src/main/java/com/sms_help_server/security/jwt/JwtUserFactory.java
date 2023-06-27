package com.sms_help_server.security.jwt;

import com.sms_help_server.entities.base.EntityStatus;
import com.sms_help_server.entities.role.Role;
import com.sms_help_server.entities.user.SmsHelpUser;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class JwtUserFactory {
    public static JwtUser create(SmsHelpUser user) {
        return new JwtUser(
                user.getUserId(),
                user.getNickname(),
                user.getEmail(),
                user.getPassword(),
                user.getEntityStatus().equals(EntityStatus.ACTIVE),
                user.getUpdatedDate(),
                mapToGrantedAuthorities(user.getRoles())
        );
    }

    private static List<SimpleGrantedAuthority> mapToGrantedAuthorities(List<Role> userRoles) {
        return userRoles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }
}

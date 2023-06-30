package com.sms_help_server.security.config;

import com.sms_help_server.entities.role.RoleName;
import com.sms_help_server.security.jwt.JwtConfigurer;
import com.sms_help_server.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String USER_ENDPOINTS = "/api/v1/user/**";
    private static final String ANDMIN_ENDPOINTS = "/api/v1/admin/**";
    private static final String AUTH_ENDPOINTS = "/api/v1/auth/**";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    // intellij idea cant recognize variable names in Spring EL expressions and throws warnings,
    // but all works fine.
    @SuppressWarnings("all")
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .httpBasic()
                .disable()
                .csrf()
                .disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_ENDPOINTS).permitAll()
                .antMatchers(ANDMIN_ENDPOINTS).hasRole(RoleName.ROLE_ADMIN.getValue())
                .antMatchers(USER_ENDPOINTS)
                    .access(
                            "hasRole('" + RoleName.ROLE_ADMIN.getValue() + "') " +
                            "or @userSecurity.valudateUser(authentication, request)"
                    )
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));
    }
}

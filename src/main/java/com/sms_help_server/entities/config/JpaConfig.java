package com.sms_help_server.entities.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAwareBean")
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAwareBean() {
        return new AuditorAwareImpl();
    }
}

package com.sms_help_server.repo;

import com.sms_help_server.entities.user.user_entity.SmsHelpUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<SmsHelpUser, Long> {
    Optional<SmsHelpUser> findByEmail(String email);
}


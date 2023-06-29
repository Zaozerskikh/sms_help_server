package com.sms_help_server.repo;

import com.sms_help_server.entities.phone_number.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    Optional<PhoneNumber> findByNumber(String number);

    Optional<PhoneNumber> findByNumberId(Long id);

    boolean existsByNumber(String number);
}

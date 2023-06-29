package com.sms_help_server.repo;

import com.sms_help_server.entities.service.Service;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    Optional<Service> findByName(String name);

    Optional<Service> findByServiceId(Long serviceId);
}

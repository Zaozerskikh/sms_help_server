package com.sms_help_server.repo;

import com.sms_help_server.entities.sms_pva_number_purchase.SmsPvaNumberPurchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SmsPvaPurchaseRepository extends JpaRepository<SmsPvaNumberPurchase, Long> {
    Optional<SmsPvaNumberPurchase> findBySmsPvaPurchaseId(String smsPvaId);
}

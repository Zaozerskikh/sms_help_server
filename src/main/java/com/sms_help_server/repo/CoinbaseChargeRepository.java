package com.sms_help_server.repo;

import com.sms_help_server.entities.transaction.coinbase_top_up.CoinbaseCharge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoinbaseChargeRepository extends JpaRepository<CoinbaseCharge, String> {
    Optional<CoinbaseCharge> findByCoinbaseChargeId(String coinbaseChargeId);

    boolean existsByCoinbaseChargeId(String coinbaseChargeId);
}

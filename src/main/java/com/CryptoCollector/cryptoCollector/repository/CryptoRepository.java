package com.CryptoCollector.cryptoCollector.repository;

import com.CryptoCollector.cryptoCollector.entity.Crypto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CryptoRepository extends JpaRepository<Crypto,Long> {

    Optional<Crypto> findByExternalId(String externalId);
}

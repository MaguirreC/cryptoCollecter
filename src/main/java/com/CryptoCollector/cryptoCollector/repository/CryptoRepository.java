package com.CryptoCollector.cryptoCollector.repository;

import com.CryptoCollector.cryptoCollector.entity.Crypto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CryptoRepository extends JpaRepository<Crypto,Long> {

    Optional<Crypto> findByExternalId(String externalId);


    Page<Crypto> findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(String name, String symbol, Pageable pageable);

    @Query("SELECT c FROM Crypto c WHERE " +
            "(:minPrice IS NULL OR c.priceUsd >= :minPrice) AND " +
            "(:maxPrice IS NULL OR c.priceUsd <= :maxPrice) AND " +
            "(:minMarketCap IS NULL OR c.marketCap >= :minMarketCap) AND " +
            "(:maxMarketCap IS NULL OR c.marketCap <= :maxMarketCap)")
    Page<Crypto> filterCryptos(
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("minMarketCap") Double minMarketCap,
            @Param("maxMarketCap") Double maxMarketCap,
            Pageable pageable
    );
}

package com.CryptoCollector.cryptoCollector.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "cryptos")
public class Crypto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String externalId;
    private String name;
    private String symbol;
    private Double priceUsd;
    private Double marketCap;
    private Double volume24h;
    private Double high24h;
    private Double low24h;
    private Double priceChange24h;
}

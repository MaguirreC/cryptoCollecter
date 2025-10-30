package com.CryptoCollector.cryptoCollector.service;


import com.CryptoCollector.cryptoCollector.entity.Crypto;
import com.CryptoCollector.cryptoCollector.repository.CryptoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CryptoService {

    private final CryptoRepository cryptoRepository;
    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${api.coingecko.url}")
    private String apiUrl;

    @Transactional
    @CacheEvict(value = {"cryptos", "cryptoByExternalId"}, allEntries = true)
    @Scheduled(fixedRate = 3600000, initialDelay = 3600000)
    public void syncData(){
        // Cargar todos los external IDs existentes de una vez
        Map<String, Crypto> existingCryptos = cryptoRepository.findAll()
                .stream()
                .collect(Collectors.toMap(Crypto::getExternalId, crypto -> crypto));

        List<Crypto> cryptosToSave = new ArrayList<>();

        for(int page = 1; page <= 4; page++) {
            String url = apiUrl + "?vs_currency=usd&order=market_cap_desc&per_page=250&page=" + page;
            List<Map<String, Object>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {
                    }
            ).getBody();

            if (response != null){
                response.forEach(data -> {
                    String id = (String) data.get("id");
                    Crypto crypto = existingCryptos.getOrDefault(id, new Crypto());

                    crypto.setExternalId(id);
                    crypto.setName((String) data.get("name"));
                    crypto.setSymbol((String) data.get("symbol"));
                    crypto.setPriceUsd(getDoubleSafe(data, "current_price"));
                    crypto.setMarketCap(getDoubleSafe(data, "market_cap"));
                    crypto.setVolume24h(getDoubleSafe(data, "total_volume"));
                    crypto.setHigh24h(getDoubleSafe(data, "high_24h"));
                    crypto.setLow24h(getDoubleSafe(data, "low_24h"));
                    crypto.setPriceChange24h(getDoubleSafe(data, "price_change_24h"));

                    cryptosToSave.add(crypto);
                });
            }
        }

        // Guardar todos de una vez
        cryptoRepository.saveAll(cryptosToSave);
    }

    @Cacheable(value = "cryptoByExternalId", key = "#externalId")
    public Crypto findByExternalId(String externalId) {
        return cryptoRepository.findByExternalId(externalId)
                .orElse(null);
    }

    private String getStringSafe(Map<String, Object> data, String key) {
        Object value = data.get(key);
        return value != null ? value.toString() : "0";
    }
    private Double getDoubleSafe(Map<String, Object> data, String key) {
        Object value = data.get(key);
        if (value == null) return 0.0;
        if (value instanceof Number) return ((Number) value).doubleValue();
        try {
            return Double.parseDouble(value.toString());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public Page<Crypto> filterCryptos(
            Double minPrice,
            Double maxPrice,
            Double minMarketCap,
            Double maxMarketCap,
            Pageable pageable
    ) {
        return cryptoRepository.filterCryptos(minPrice, maxPrice, minMarketCap, maxMarketCap, pageable);
    }


}

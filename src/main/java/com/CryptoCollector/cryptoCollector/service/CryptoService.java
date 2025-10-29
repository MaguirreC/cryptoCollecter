package com.CryptoCollector.cryptoCollector.service;


import com.CryptoCollector.cryptoCollector.entity.Crypto;
import com.CryptoCollector.cryptoCollector.repository.CryptoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CryptoService {

    private final CryptoRepository cryptoRepository;
    private final RestTemplate restTemplate = new RestTemplate();


    @Value("${api.coingecko.url}")
    private String apiUrl;


    public void syncData(){
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
                response.forEach(this::saveOrUpdateCrypto);
            }
        }
    }

    private void saveOrUpdateCrypto(Map<String, Object> data) {

        String id = (String) data.get("id");

        Crypto crypto = cryptoRepository.findByExternalId(id)
                .orElseGet(Crypto::new);

        crypto.setExternalId(id);
        crypto.setName((String) data.get("name"));
        crypto.setSymbol((String) data.get("symbol"));
        crypto.setPriceUsd(String.valueOf(data.get("current_price").toString()));
        cryptoRepository.save(crypto);
    }



}

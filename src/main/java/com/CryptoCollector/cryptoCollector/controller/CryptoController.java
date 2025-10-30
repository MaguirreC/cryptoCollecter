package com.CryptoCollector.cryptoCollector.controller;


import com.CryptoCollector.cryptoCollector.entity.Crypto;
import com.CryptoCollector.cryptoCollector.exception.ResourceNotFoundException;
import com.CryptoCollector.cryptoCollector.repository.CryptoRepository;
import com.CryptoCollector.cryptoCollector.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.web.servlet.function.ServerResponse.ok;

@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
public class CryptoController {

    private final CryptoService cryptoService;

    private final CryptoRepository cryptoRepository;

    @GetMapping("/sync")
    public ResponseEntity<?> sync() {
        cryptoService.syncData();
        return ResponseEntity.ok(
                Map.of(
                        "status", "success",
                        "message", "Sincronización completada exitosamente"
                )
        );
    }

        @GetMapping("/list")
    public Page<Crypto> listCryptos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ){
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return cryptoRepository.findAll(pageable);
    }

    @GetMapping("/search")
    public Page<Crypto> search(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<Crypto> results = cryptoRepository.findByNameContainingIgnoreCaseOrSymbolContainingIgnoreCase(
                query, query, pageable
        );

        if (results.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron criptomonedas que coincidan con: " + query);
        }

        return results;
    }


    @GetMapping("/{id}")
    public Crypto getById(@PathVariable Long id) {
                return cryptoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Crypto no encontrada con id: " + id));
    }

    @GetMapping("/filter")
    public Page<Crypto> filter(
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Double minMarketCap,
            @RequestParam(required = false) Double maxMarketCap,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "marketCap") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ){
        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return cryptoService.filterCryptos(minPrice, maxPrice, minMarketCap, maxMarketCap, pageable);
    }

}

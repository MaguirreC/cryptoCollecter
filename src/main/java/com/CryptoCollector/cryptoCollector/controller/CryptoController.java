package com.CryptoCollector.cryptoCollector.controller;


import com.CryptoCollector.cryptoCollector.service.CryptoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/crypto")
@RequiredArgsConstructor
public class CryptoController {

    private final CryptoService cryptoService;


    public String sync(){
        cryptoService.syncData();
        return "Datos sincronizados correctamente";
    }
}

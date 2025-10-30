package com.CryptoCollector.cryptoCollector.controller;

import com.CryptoCollector.cryptoCollector.entity.User;
import com.CryptoCollector.cryptoCollector.payload.AuthRequest;
import com.CryptoCollector.cryptoCollector.repository.UserRepository;
import com.CryptoCollector.cryptoCollector.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public String register(@RequestBody AuthRequest authRequest){
        if(userRepository.findByUsername(authRequest.getUsername()).isPresent()){
            return "El nombre de usuario ya existe";
        }
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        userRepository.save(user);
        return "Usuario registrado correctamente";
    }

    @PostMapping("/login")
    public Map<String,String> login(@RequestBody AuthRequest authRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword())
        );
        String token = jwtProvider.generateToken(new UsernamePasswordAuthenticationToken(authRequest.getUsername(),authRequest.getPassword()));
        return Map.of("token",token);
    }


}

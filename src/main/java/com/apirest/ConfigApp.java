package com.apirest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class ConfigApp {
    private final String host= "http://localhost:8080";
    @Bean
    public WebClient webClient(){
        String username = "user";
        String password = "d241f78d-6ab7-41d7-a29c-15839989a644";
        String auth = username + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        String authHeader = "Basic " + encodedAuth;

        return WebClient.builder()
                .baseUrl(host)
                .defaultHeader("Authorization", authHeader)
                .build();
    }
}

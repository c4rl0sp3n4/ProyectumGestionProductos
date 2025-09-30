package com.proyectum.gestion.productos.service;

import com.proyectum.gestion.productos.dto.ExchangeRatesResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class ExchangeRateService {

    private final WebClient webClient;

    public ExchangeRateService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://api.exchangerate-api.com/v4/latest")
                .build();
    }

    public Mono<BigDecimal> getExchangeRateTo(String targetCurrency) {
        return webClient.get()
                .uri("/USD") 
                .retrieve()
                .bodyToMono(ExchangeRatesResponse.class)
                .map(response -> {
                    Map<String, BigDecimal> rates = response.getRates();
                    BigDecimal rate = rates.get(targetCurrency.toUpperCase());
                    if (rate == null) {
                        throw new IllegalArgumentException("Moneda no soportada: " + targetCurrency);
                    }
                    return rate;
                });
    }
}
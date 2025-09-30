package com.proyectum.gestion.test;

import com.proyectum.gestion.productos.dto.ProductoConverted;
import com.proyectum.gestion.productos.model.Producto;
import com.proyectum.gestion.productos.repository.ProductoRepository;
import com.proyectum.gestion.productos.service.ExchangeRateService;
import com.proyectum.gestion.productos.service.ProductoService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private ExchangeRateService exchangeRateService;

    @InjectMocks
    private ProductoService productoService;

    @Test
    void shouldConvertProductosToTargetCurrency() {

        List<Producto> products = Arrays.asList(
                new Producto("Laptop", new BigDecimal("1000.00"), 5),
                new Producto("Mouse", new BigDecimal("20.00"), 2)
        );

        BigDecimal exchangeRate = new BigDecimal("0.93");
        String targetCurrency = "EUR";


        when(productoRepository.findAll()).thenReturn(Flux.fromIterable(products));


        when(exchangeRateService.getExchangeRateTo(targetCurrency))
                .thenReturn(Mono.just(exchangeRate));


        Flux<ProductoConverted> result = productoService.getProductosConvertedTo(targetCurrency);


        StepVerifier.create(result)
                .expectNextMatches(pc -> 
                        pc.name().equals("Laptop") &&
                        pc.originalPrice().compareTo(new BigDecimal("1000.00")) == 0 &&
                        pc.convertedPrice().compareTo(new BigDecimal("930.00")) == 0 &&
                        pc.targetCurrency().equals("EUR")
                )
                .expectNextMatches(pc -> 
                        pc.name().equals("Mouse") &&
                        pc.originalPrice().compareTo(new BigDecimal("20.00")) == 0 &&
                        pc.convertedPrice().compareTo(new BigDecimal("18.60")) == 0 &&
                        pc.targetCurrency().equals("EUR")
                )
                .verifyComplete();
    }

    @Test
    void shouldReturnEmptyWhenNoProducts() {
        // Given
        String targetCurrency = "JPY";
        BigDecimal rate = new BigDecimal("150.00");

        when(productoRepository.findAll()).thenReturn(Flux.empty());
        when(exchangeRateService.getExchangeRateTo(targetCurrency))
                .thenReturn(Mono.just(rate));


        Flux<ProductoConverted> result = productoService.getProductosConvertedTo(targetCurrency);


        StepVerifier.create(result)
                .expectComplete()
                .verify();
    }

}
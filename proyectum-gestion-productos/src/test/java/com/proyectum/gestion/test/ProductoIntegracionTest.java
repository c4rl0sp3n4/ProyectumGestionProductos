package com.proyectum.gestion.test;

import com.proyectum.gestion.productos.ProyectumGestionProductosApplication;
import com.proyectum.gestion.productos.model.Producto;
import com.proyectum.gestion.productos.repository.ProductoRepository;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = ProyectumGestionProductosApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class ProductoIntegracionTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductoRepository productoRepository;

    private MockWebServer mockExchangeApi;

    @BeforeEach
    void setUp() throws IOException {
        mockExchangeApi = new MockWebServer();
        mockExchangeApi.start();

        String baseUrl = "http://localhost:" + mockExchangeApi.getPort();
        System.setProperty("exchange.rate.api.url", baseUrl);

        productoRepository.deleteAll().block();
        Flux.fromIterable(java.util.List.of(
                new Producto("Laptop", new BigDecimal("1000.00"), 5),
                new Producto("Mouse", new BigDecimal("20.00"), 2)
        )).flatMap(productoRepository::save).blockLast();
    }

    @AfterEach
    void tearDown() throws IOException {
        mockExchangeApi.shutdown();
        System.clearProperty("exchange.rate.api.url");
    }

    @Test
    void shouldReturnProductosConvertedToEUR() {

        webTestClient.get()
                .uri("/api/productos/converted/EUR")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // Then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[0].name").isEqualTo("Laptop")
                .jsonPath("$[0].originalPrice").isEqualTo(1000.00)
                .jsonPath("$[0].convertedPrice").isEqualTo(853.00) 
                .jsonPath("$[1].name").isEqualTo("Mouse")
                .jsonPath("$[1].convertedPrice").isEqualTo(17.06); 

        assertThat(mockExchangeApi.getRequestCount()).isEqualTo(0);
    }

    @Test
    void shouldReturnBadRequestWhenCurrencyNotSupported() {
        String mockApiResponse = """
        {
          "base": "USD",
          "rates": {
            "EUR": 0.93
          }
        }
        """;

        mockExchangeApi.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody(mockApiResponse)
                .setHeader("Content-Type", "application/json"));

        webTestClient.get()
                .uri("/api/productos/converted/XYZ")
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(String.class)
                .value(message -> assertThat(message).contains("Moneda no soportada"));
    }
}
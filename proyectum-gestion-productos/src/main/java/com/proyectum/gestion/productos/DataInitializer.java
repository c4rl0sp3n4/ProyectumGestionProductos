package com.proyectum.gestion.productos;

import com.proyectum.gestion.productos.model.Producto;
import com.proyectum.gestion.productos.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductoRepository productoRepository;

    public DataInitializer(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public void run(String... args) {
        productoRepository.deleteAll()
                .thenMany(
                        Flux.just(
                                new Producto("Laptop", new BigDecimal("1200.00"), 10),
                                new Producto("Mouse", new BigDecimal("25.00"), 3),
                                new Producto("Teclado", new BigDecimal("75.00"), 2),
                                new Producto("Monitor", new BigDecimal("300.00"), 7)
                        )
                        .flatMap(productoRepository::save)
                )
                .subscribe();
    }
}

package com.proyectum.gestion.productos.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.proyectum.gestion.productos.model.Producto;

import reactor.core.publisher.Flux;

public interface ProductoRepository extends ReactiveCrudRepository<Producto, Long> {

    @Query("SELECT * FROM productos WHERE stock < 5")
    Flux<Producto> findAllWithBajoStock();

}

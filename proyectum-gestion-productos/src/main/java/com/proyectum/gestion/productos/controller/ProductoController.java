package com.proyectum.gestion.productos.controller;

import com.proyectum.gestion.productos.dto.ProductoConverted;
import com.proyectum.gestion.productos.model.Producto;
import com.proyectum.gestion.productos.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public Flux<Producto> getAllProductos() {
        return productoService.findAll();
    }

    @GetMapping("/bajo-stock")
    public Flux<Producto> getLowStockProductos() {
        return productoService.findBajoStockProductos();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Producto>> getProductoById(@PathVariable Long id) {
        return productoService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Producto> createProduct(@RequestBody Producto producto) {
        return productoService.save(producto);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Producto>> updateProducto(
            @PathVariable Long id,
            @RequestBody Producto producto) {
        return productoService.update(id, producto)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteProducto(@PathVariable Long id) {
        return productoService.deleteById(id);
    }
    
    @GetMapping("/converted/{moneda}")
    public Flux<ProductoConverted> getProductsInCurrency(@PathVariable String moneda) {
        return productoService.getProductosConvertedTo(moneda);
    }    
}
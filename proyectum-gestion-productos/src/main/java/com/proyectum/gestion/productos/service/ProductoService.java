package com.proyectum.gestion.productos.service;

import com.proyectum.gestion.productos.dto.ProductoConverted;
import com.proyectum.gestion.productos.model.Producto;
import com.proyectum.gestion.productos.repository.ProductoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final ExchangeRateService exchangeRateService;

    public ProductoService(ProductoRepository productoRepository,
            ExchangeRateService exchangeRateService) {
    	this.productoRepository = productoRepository;
    	this.exchangeRateService = exchangeRateService;
    }    
    
    public Flux<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Mono<Producto> findById(Long id) {
        return productoRepository.findById(id);
    }

    public Mono<Producto> save(Producto producto) {
        return productoRepository.save(producto);
    }

    public Mono<Producto> update(Long id, Producto updatedProducto) {
        return productoRepository.findById(id)
                .flatMap(existing -> {
                    existing.setNombre(updatedProducto.getNombre());
                    existing.setPrecio(updatedProducto.getPrecio());
                    existing.setStock(updatedProducto.getStock());
                    return productoRepository.save(existing);
                });
    }

    public Mono<Void> deleteById(Long id) {
        return productoRepository.deleteById(id);
    }

    public Flux<Producto> findBajoStockProductos() {
        return productoRepository.findAllWithBajoStock();
    }
    
    public Flux<ProductoConverted> getProductosConvertedTo(String targetCurrency) {
        String originalCurrency = "USD"; 

        return exchangeRateService.getExchangeRateTo(targetCurrency)
                .flatMapMany(rate -> productoRepository.findAll()
                        .map(producto -> {
                            BigDecimal converted = producto.getPrecio()
                                    .multiply(rate)
                                    .setScale(2, RoundingMode.HALF_UP);
                            return new ProductoConverted(
                                    producto.getId(),
                                    producto.getNombre(),
                                    producto.getPrecio(),
                                    originalCurrency,
                                    converted,
                                    targetCurrency.toUpperCase()
                            );
                        }));
    }    
}

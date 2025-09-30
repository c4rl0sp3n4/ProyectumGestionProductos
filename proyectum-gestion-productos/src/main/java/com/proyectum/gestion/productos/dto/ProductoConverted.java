package com.proyectum.gestion.productos.dto;

import java.math.BigDecimal;

public record ProductoConverted(
        Long id,
        String name,
        BigDecimal originalPrice,
        String originalCurrency,
        BigDecimal convertedPrice,
        String targetCurrency
) {}
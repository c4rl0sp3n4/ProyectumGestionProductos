package com.proyectum.gestion.productos.dto;

import java.math.BigDecimal;
import java.util.Map;

public class ExchangeRatesResponse {
    private String base;
    private Map<String, BigDecimal> rates;


    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }

    public Map<String, BigDecimal> getRates() { return rates; }
    public void setRates(Map<String, BigDecimal> rates) { this.rates = rates; }
}

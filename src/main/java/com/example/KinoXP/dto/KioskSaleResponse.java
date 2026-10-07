package com.example.KinoXP.dto;

import com.example.KinoXP.model.KioskSale;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record KioskSaleResponse(Long id, Long productId, String productName, int quantity,
                                BigDecimal unitPrice, BigDecimal total, LocalDateTime saleTime) {

    public static KioskSaleResponse from(KioskSale sale) {
        return new KioskSaleResponse(
                sale.getId(),
                sale.getProduct().getId(),
                sale.getProduct().getProduct(),
                sale.getQuantity(),
                sale.getUnitPrice(),
                sale.getTotal(),
                sale.getSaleTime()
        );
    }
}
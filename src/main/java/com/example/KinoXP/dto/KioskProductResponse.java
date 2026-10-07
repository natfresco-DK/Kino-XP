package com.example.KinoXP.dto;

import com.example.KinoXP.model.KioskProduct;

import java.math.BigDecimal;

public record KioskProductResponse(Long id, String product, BigDecimal price) {

    public static KioskProductResponse from(KioskProduct kioskProduct) {
        return new KioskProductResponse(
                kioskProduct.getId(),
                kioskProduct.getProduct(),
                kioskProduct.getPrice()
        );
    }
}
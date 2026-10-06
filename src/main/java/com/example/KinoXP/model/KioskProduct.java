package com.example.KinoXP.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class KioskProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false, unique = true)
    private String product;

    public KioskProduct() {
    }

    public KioskProduct(BigDecimal price, String product) {
        this.price = price;
        this.product = product;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getProduct() {
        return product;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
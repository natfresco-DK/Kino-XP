package com.example.KinoXP.dto;

import java.math.BigDecimal;

public record KioskProductRequest(String product, BigDecimal price) {
}
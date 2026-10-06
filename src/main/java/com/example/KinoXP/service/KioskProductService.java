package com.example.KinoXP.service;

import com.example.KinoXP.dto.KioskProductRequest;
import com.example.KinoXP.dto.KioskProductResponse;
import com.example.KinoXP.model.KioskProduct;
import com.example.KinoXP.repository.KioskProductRepo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class KioskProductService {
    private static final int MAX_NAME_LENGTH = 255;
    private static final BigDecimal MAX_PRICE = new BigDecimal("99999999.99");

    private final KioskProductRepo kioskProductRepo;

    public KioskProductService(KioskProductRepo kioskProductRepo) {
        this.kioskProductRepo = kioskProductRepo;
    }

    public KioskProduct createProduct(KioskProductRequest request) {
        validate(request);

        String name = request.product().trim();

        if (kioskProductRepo.existsByProductIgnoreCase(name)) {
            throw new IllegalArgumentException("Produktet findes allerede");
        }

        KioskProduct kioskProduct = new KioskProduct(request.price(), name);
        return kioskProductRepo.save(kioskProduct);
    }

    public List<KioskProductResponse> getAllProducts() {
        return kioskProductRepo.findAll().stream().map(KioskProductResponse::from).toList();
    }

    private void validate(KioskProductRequest request) {
        if (request.product() == null || request.product().isBlank()) {
            throw new IllegalArgumentException("Tilføj et produktnavn");
        }
        if (request.product().trim().length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("Produktnavnet må højst være " + MAX_NAME_LENGTH + " tegn");
        }

        if (request.price() == null || request.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Prisen skal være større end 0");
        }
        if (request.price().compareTo(MAX_PRICE) > 0) {
            throw new IllegalArgumentException("Prisen er for høj");
        }

        if (request.price().stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException("Prisen må højst have 2 decimaler");
        }
    }
}
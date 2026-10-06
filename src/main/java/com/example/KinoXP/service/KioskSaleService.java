package com.example.KinoXP.service;


import com.example.KinoXP.dto.CreateKioskSaleRequest;
import com.example.KinoXP.dto.KioskSaleResponse;
import com.example.KinoXP.model.KioskProduct;
import com.example.KinoXP.model.KioskSale;
import com.example.KinoXP.repository.KioskProductRepo;
import com.example.KinoXP.repository.KioskSaleRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class KioskSaleService {

    private final KioskSaleRepo kioskSaleRepo;
    private final KioskProductRepo kioskProductRepo;

    public KioskSaleService(KioskSaleRepo kioskSaleRepo, KioskProductRepo kioskProductRepo) {
        this.kioskSaleRepo = kioskSaleRepo;
        this.kioskProductRepo = kioskProductRepo;
    }

    @Transactional
    public KioskSale createSale(CreateKioskSaleRequest request) {
        if (request.quantity() == null || request.quantity() <= 0) {
            throw new IllegalArgumentException("Antal skal være mindst 1");
        }

        KioskProduct product = getProductOrThrow(request.productId());

        KioskSale sale = new KioskSale(product, request.quantity());
        return kioskSaleRepo.save(sale);
    }

    public List<KioskSaleResponse> getAllSales() {
        return kioskSaleRepo.findAll()
                .stream()
                .map(KioskSaleResponse::from)
                .toList();
    }

    private KioskProduct getProductOrThrow(Long productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Vælg et produkt");
        }
        return kioskProductRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Produkt ikke fundet"));
    }

}



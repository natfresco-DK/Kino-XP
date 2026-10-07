package com.example.KinoXP.controller;

import com.example.KinoXP.dto.CreateKioskSaleRequest;
import com.example.KinoXP.dto.KioskSaleResponse;
import com.example.KinoXP.model.KioskSale;
import com.example.KinoXP.service.KioskSaleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class KioskSaleController {

    private final KioskSaleService kioskSaleService;

    public KioskSaleController(KioskSaleService kioskSaleService) {
        this.kioskSaleService = kioskSaleService;
    }

    @PostMapping("/kiosk/sales")
    public ResponseEntity<KioskSaleResponse> createSale(@RequestBody CreateKioskSaleRequest request) {
        KioskSale sale = kioskSaleService.createSale(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(KioskSaleResponse.from(sale));
    }

    @GetMapping("/kiosk/sales")
    public ResponseEntity<List<KioskSaleResponse>> getAllSales() {
        return ResponseEntity.ok(kioskSaleService.getAllSales());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", e.getMessage()));
    }
}
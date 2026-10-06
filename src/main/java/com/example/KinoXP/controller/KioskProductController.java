package com.example.KinoXP.controller;

import com.example.KinoXP.dto.KioskProductRequest;
import com.example.KinoXP.dto.KioskProductResponse;
import com.example.KinoXP.model.KioskProduct;
import com.example.KinoXP.service.KioskProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class KioskProductController {

    private final KioskProductService kioskProductService;

    public KioskProductController(KioskProductService kioskProductService) {
        this.kioskProductService = kioskProductService;
    }

    @PostMapping("/kiosk/products")
    public ResponseEntity<KioskProductResponse> createProduct(@RequestBody KioskProductRequest request) {
        KioskProduct product = kioskProductService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(KioskProductResponse.from(product));
    }

    @GetMapping("/kiosk/products")
    public ResponseEntity<List<KioskProductResponse>> getAllProducts() {
        return ResponseEntity.ok(kioskProductService.getAllProducts());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity
                .badRequest()
                .body(Map.of("message", e.getMessage()));
    }
}
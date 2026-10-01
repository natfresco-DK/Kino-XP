package com.example.KinoXP.controller;

import com.example.KinoXP.dto.CreateScreeningRequest;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.service.ScreeningService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(
            ScreeningService screeningService) {

        this.screeningService = screeningService;
    }

    @PostMapping("/screenings")
    public ResponseEntity<Void> createScreening(
            @RequestBody CreateScreeningRequest request) {
        try{
        Optional<Screening> screening =
                screeningService.createScreening(request);

        if (screening.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();

    }catch(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }
}
}
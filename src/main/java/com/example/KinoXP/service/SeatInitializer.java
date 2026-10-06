package com.example.KinoXP.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeatInitializer implements CommandLineRunner {

    private final SeatService seatService;

    public SeatInitializer(SeatService seatService) {
        this.seatService = seatService;
    }

    @Override
    public void run(String... args) {

        // Lille Sal
        seatService.createSeatsForScreen(1L);

        // Store Sal
        seatService.createSeatsForScreen(2L);
    }
}
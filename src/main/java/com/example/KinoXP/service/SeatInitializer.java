package com.example.KinoXP.service;

import com.example.KinoXP.model.Screen;
import com.example.KinoXP.repository.ScreenRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SeatInitializer implements CommandLineRunner {

    private final SeatService seatService;
    private final ScreenRepo screenRepo;

    public SeatInitializer(SeatService seatService, ScreenRepo screenRepo) {
        this.seatService = seatService;
        this.screenRepo = screenRepo;
    }

    @Override
    public void run(String... args) {
        for (Screen screen : screenRepo.findAll()) {
            if (screen.getScreenType() != null) {
                seatService.createSeatsForScreen(screen.getId());
            }
        }
    }
}
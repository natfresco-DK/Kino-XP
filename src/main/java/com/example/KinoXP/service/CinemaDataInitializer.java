package com.example.KinoXP.service;

import com.example.KinoXP.model.Screen;
import com.example.KinoXP.repository.ScreenRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CinemaDataInitializer implements CommandLineRunner {

    private final ScreenRepo screenRepo;
    private final SeatService seatService;

    public CinemaDataInitializer(ScreenRepo screenRepo, SeatService seatService) {
        this.screenRepo = screenRepo;
        this.seatService = seatService;
    }

    @Override
    public void run(String... args) {

        Screen smallScreen = screenRepo.findByName("Lille").orElse(null);

        if (smallScreen == null) {
            smallScreen = screenRepo.save(
                    new Screen("Lille", 20, 12)
            );
        }

        Screen largeScreen = screenRepo.findByName("Stor").orElse(null);

        if (largeScreen == null) {
            largeScreen = screenRepo.save(
                    new Screen("Stor", 25, 16)
            );
        }

        seatService.createSeatsForScreen(smallScreen.getId());
        seatService.createSeatsForScreen(largeScreen.getId());
    }
}
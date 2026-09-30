package com.example.KinoXP.controller;

import com.example.KinoXP.dto.CreateScreeningRequest;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.service.MovieService;
import com.example.KinoXP.service.ScreeningService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ScreeningController {

    private final ScreeningService screeningService;
    private final MovieService movieService;

    public ScreeningController(
            ScreeningService screeningService, MovieService movieService) {

        this.screeningService = screeningService;
        this.movieService = movieService;
    }

    @PostMapping("/screenings")
    public ResponseEntity<Void> createScreening(
            @RequestBody CreateScreeningRequest request) {

        Optional<Screening> screening =
                screeningService.createScreening(request);

        if (screening.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
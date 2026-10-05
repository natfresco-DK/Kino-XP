package com.example.KinoXP.controller;

import com.example.KinoXP.dto.CreateScreeningRequest;
import com.example.KinoXP.dto.ScreeningResponse;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.service.ScreeningService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
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
    @GetMapping("/screenings")
    public ResponseEntity<List<ScreeningResponse>> getUpcomingScreenings() {
        List<ScreeningResponse> screenings = screeningService.getUpcomingScreenings();
        return ResponseEntity.ok(screenings);
    }

    @GetMapping("/screenings/date/{date}")
    public ResponseEntity<List<ScreeningResponse>> getScreeningsByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        List<ScreeningResponse> screenings = screeningService.getScreeningsByDate(date);
        return ResponseEntity.ok(screenings);
    }

    @GetMapping("/screenings/movie/{movieId}")
    public ResponseEntity<List<ScreeningResponse>> getUpcomingScreeningsForMovie(
            @PathVariable Long movieId) {
        List<ScreeningResponse> screenings = screeningService.getUpcomingScreeningsForMovie(movieId);
        return ResponseEntity.ok(screenings);
    }
}
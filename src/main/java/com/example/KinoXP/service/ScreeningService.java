package com.example.KinoXP.service;

import com.example.KinoXP.dto.CreateScreeningRequest;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.model.Screen;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.repository.ScreenRepo;
import com.example.KinoXP.repository.ScreeningRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ScreeningService {

    private final ScreeningRepo screeningRepo;
    private final MovieRepo movieRepo;
    private final ScreenRepo screenRepo;

    public ScreeningService(
            ScreeningRepo screeningRepo,
            MovieRepo movieRepo,
            ScreenRepo screenRepo) {

        this.screeningRepo = screeningRepo;
        this.movieRepo = movieRepo;
        this.screenRepo = screenRepo;
    }

    public Optional<Screening> createScreening(
            CreateScreeningRequest request) {
        if(!request.endTime().isAfter(request.startTime())) {
            throw new IllegalArgumentException(
                    "End time must be after start time"
            );
        }
        Optional<Movie> movieResult = movieRepo.findById(request.movieId());

        if (movieResult.isEmpty()) {
            return Optional.empty();
        }

        Optional<Screen> screenResult = screenRepo.findById(request.screenId());

        if (screenResult.isEmpty()) {
            return Optional.empty();
        }

        Movie movie = movieResult.get();
        Screen screen = screenResult.get();

        Screening screening = new Screening(
                movie,
                screen,
                request.startTime(),
                request.endTime()
        );

        Screening savedScreening = screeningRepo.save(screening);

        return Optional.of(savedScreening);
    }
}
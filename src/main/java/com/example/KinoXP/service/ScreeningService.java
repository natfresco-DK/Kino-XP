package com.example.KinoXP.service;

import com.example.KinoXP.dto.CreateScreeningRequest;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.model.Screen;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.repository.ScreenRepo;
import com.example.KinoXP.repository.ScreeningRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

        Movie movie = getMovieOrThrow(request.movieId());

        Screen screen = getScreenOrThrow(request.screenId());

        validateNoOverlap(screen, request.startTime(), request.endTime());

        Screening screening = new Screening(
                movie,
                screen,
                request.startTime(),
                request.endTime()
        );

        Screening savedScreening = screeningRepo.save(screening);

        return Optional.of(savedScreening);
    }

    private void validateNoOverlap(Screen screen, LocalDateTime startTime, LocalDateTime endTime) {
        boolean isOverlapping = screeningRepo.isOverlapping(screen, startTime, endTime);
        if (isOverlapping) {
            throw new IllegalArgumentException(
                    "Screening overlaps with an existing screening"
            );
        }
    }

    private Movie getMovieOrThrow(Long movieId) {
        return movieRepo.findById(movieId)
                .orElseThrow(() ->
                        //her kunne man lave en custom exception, men det er ikke nødvendigt for nu
                        new IllegalArgumentException("Movie not found")
                );
    }

    private Screen getScreenOrThrow(Long screenId) {
        return screenRepo.findById(screenId)
                .orElseThrow(() ->
                        //her kunne man lave en custom exception, men det er ikke nødvendigt for nu
                        new IllegalArgumentException("Screen not found")
                );
    }
}
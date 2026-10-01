package com.example.KinoXP.service;

import com.example.KinoXP.dto.MovieRequest;
import com.example.KinoXP.dto.MovieResponse;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.repository.MovieRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Year;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepo movieRepo;

    public MovieService(MovieRepo movieRepo) {
        this.movieRepo = movieRepo;
    }

    public MovieResponse createMovie(MovieRequest request) {

        validate(request);

        Movie movie = new Movie(
                null,
                request.title().trim(),
                request.description().trim(),
                request.genre(),
                Duration.ofMinutes(request.duration()),
                request.actors(),
                request.releaseYear(),
                request.ageLimit()
        );

        Movie savedMovie = movieRepo.save(movie);

        return new MovieResponse(
                savedMovie.getId(),
                savedMovie.getTitle(),
                savedMovie.getDescription(),
                savedMovie.getGenre(),
                savedMovie.getDuration().toMinutes(),
                savedMovie.getActors(),
                savedMovie.getReleaseYear(),
                savedMovie.getAgeLimit()
        );
    }

    private void validate(MovieRequest request) {

        if (isBlank(request.title())) {
            throw badRequest("Filmen skal have en titel");
        }

        if (isBlank(request.description())) {
            throw badRequest("Filmen skal have en beskrivelse");
        }

        if (isEmptyList(request.genre())) {
            throw badRequest("Filmen skal have mindst én genre");
        }

        if (request.duration() == null || request.duration() <= 30) {
            throw badRequest("Varighed skal være mindst 31 minutter");
        }

        if (isEmptyList(request.actors())) {
            throw badRequest("Filmen skal have mindst én skuespiller");
        }

        for (String actor : request.actors()) {

            if (!actor.trim().matches("^[a-zæøåÆØÅA-Z '-]+$")) {
                throw badRequest(
                        "Skuespillernavne må kun indeholde bogstaver, mellemrum, bindestreg og apostrof"
                );
            }
        }

        int currentYear = Year.now().getValue();

        if (request.releaseYear() < 1888 ||
                request.releaseYear() > currentYear + 5) {

            throw badRequest(
                    "Udgivelsesår skal være mellem 1888 og "
                            + (currentYear + 5)
            );
        }

        if (request.ageLimit() == null) {
            throw badRequest(
                    "Filmen skal have en aldersgrænse"
            );
        }
    }

    private boolean isBlank(String text) {
        return text == null || text.isBlank();
    }

    private boolean isEmptyList(List<String> list) {
        return list == null ||
                list.stream().allMatch(this::isBlank);
    }

    private ResponseStatusException badRequest(String message) {
        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                message
        );
    }
}
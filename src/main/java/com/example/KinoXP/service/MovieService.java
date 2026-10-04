package com.example.KinoXP.service;

import com.example.KinoXP.dto.MovieRequest;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.repository.MovieRepo;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;

@Service
public class MovieService {

    private final MovieRepo movieRepo;

    public MovieService(MovieRepo movieRepo) {
        this.movieRepo = movieRepo;
    }

    public Movie createMovie(MovieRequest request) {

        validate(request);
        Movie movie = new Movie(
                request.title().trim(),
                request.description().trim(),
                request.genre(),
                (request.duration()),
                request.ageLimit(),
                request.releaseYear(),
                request.actors()
        );
        return movieRepo.save(movie);
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
        if (request.duration() == null || request.duration().toMinutes() < 0 || request.duration().isZero() ) {
            throw badRequest("Varighed skal være være et positivt tal");
        }
        if (isEmptyList(request.actors())) {
            throw badRequest("Filmen skal have mindst én skuespiller");
        }
        for (String actor : request.actors()) {
            if (!actor.trim().matches("^[a-zæøåÆØÅA-Z ]+$")) {
                throw badRequest("Skuespillere må kun indeholde bogstaver");
            }
        }
        int currentYear = Year.now().getValue();
        if (request.releaseYear() < 1888 || request.releaseYear() > currentYear + 5) {
            throw badRequest("Udgivelsesår skal være mellem 1888 og " + (currentYear + 5));
        }
        if (request.ageLimit() == null) {
            throw badRequest("Filmen skal have en aldersgrænse");
        }
    }

    private boolean isBlank(String text) {
        return text == null || text.isBlank();
    }

    private boolean isEmptyList(List<String> list) {
        return list == null || list.stream().allMatch(this::isBlank);
    }

    private IllegalArgumentException badRequest(String message) {
        return new IllegalArgumentException(message);
    }
}
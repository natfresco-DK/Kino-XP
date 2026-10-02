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
import java.util.ArrayList;
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
                Duration.ofMinutes(request.duration()),
                request.ageLimit(),
                request.releaseYear(),
                request.actors()
        );

        return movieRepo.save(movie);
    }

    public List<MovieResponse> getAllMovies() {
        List<Movie> movies = movieRepo.findAll();
        List<MovieResponse> responses = new ArrayList<>();

        for (Movie movie : movies) {
            responses.add(MovieResponse.from(movie));
        }
        return responses;
    }

    public Movie updateMovie(Long id, MovieRequest request) {
        validate(request);

        Movie movie = movieRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Film ikke fundet"));
        movie.setTitle(request.title().trim());
        movie.setDescription(request.description().trim());
        movie.setGenre(request.genre());
        movie.setDuration(Duration.ofMinutes(request.duration()));
        movie.setAgeLimit(request.ageLimit());
        movie.setReleaseYear(request.releaseYear());
        movie.setActors(request.actors());

        return movieRepo.save(movie);
    }

    private void validate(MovieRequest request) {

        if (isBlank(request.title())) {
            throw badRequest("Tilføj en titel");
        }

        if (isBlank(request.description())) {
            throw badRequest("Tilføj en beskrivelse");
        }

        if (isEmptyList(request.genre())) {
            throw badRequest("Tilføj mindst én genre");
        }

        if (request.duration() == null || request.duration() <= 30) {
            throw badRequest("Varighed må ikke være mindre 31 minutter");
        }

        if (isEmptyList(request.actors())) {
            throw badRequest("Tilføj mindst én skuespiller");
        }

        for (String actor : request.actors()) {
            if (!actor.trim().matches("^[a-zæøåÆØÅA-Z ]+$")) {
                throw badRequest("Skuespillere må kun indeholde bogstaver");
            }
        }

        int currentYear = Year.now().getValue();

        if (request.releaseYear() < 1900 || request.releaseYear() > currentYear + 5) {
            throw badRequest("Udgivelsesår skal være mellem 1900 og " + (currentYear + 5));
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
package com.example.KinoXP.controller;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/api/films")
    public Movie create(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }
}

package com.example.KinoXP.controller;

import com.example.KinoXP.dto.MovieRequest;
import com.example.KinoXP.dto.MovieResponse;
import com.example.KinoXP.service.MovieService;
import org.springframework.web.bind.annotation.*;

@RestController
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/api/movies")
    public MovieResponse createMovie(@RequestBody MovieRequest request) {
        return movieService.createMovie(request);
    }
}
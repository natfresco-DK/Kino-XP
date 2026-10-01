package com.example.KinoXP.controller;

import com.example.KinoXP.dto.MovieRequest;
import com.example.KinoXP.dto.MovieResponse;
import com.example.KinoXP.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/movies")
    public String movies() {
        return "movies";
    }

    @PostMapping("/movies")
    public ResponseEntity<MovieResponse> createMovie(@RequestBody MovieRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movieService.createMovie(request));
    }
}
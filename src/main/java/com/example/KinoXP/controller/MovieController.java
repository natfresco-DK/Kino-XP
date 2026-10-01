package com.example.KinoXP.controller;

import com.example.KinoXP.dto.MovieRequest;
import com.example.KinoXP.dto.MovieResponse;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.service.MovieService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
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
    @ResponseBody
    public ResponseEntity<?> createMovie(@RequestBody MovieRequest request) {
        try {
            Movie movie = movieService.createMovie(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(MovieResponse.from(movie));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/api/movies")
    @ResponseBody
    public List<MovieResponse> getAllMovies() {
        return movieService.getAllMovies().stream()
                .map(MovieResponse::from)
                .toList();
    }

    @PutMapping("/api/movies/{id}")
    @ResponseBody
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody MovieRequest request) {
        try {
            Movie movie = movieService.updateMovie(id, request);
            return ResponseEntity.ok(MovieResponse.from(movie));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", e.getMessage()));
        }
    }
}
package com.example.KinoXP.controller;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/api/movies")
    public Movie create(@RequestBody Movie movie) {
        return movieService.createMovie(movie);
    }

    @GetMapping("/api/movies")
    public List<Movie> getAllMovies(){
        return movieService.getAllMovies();
    }

    @PutMapping("/api/movies/{id}")
    public Movie update(@PathVariable Long id, @RequestBody Movie movie) {
        return movieService.updateMovie(id, movie);
    }
}

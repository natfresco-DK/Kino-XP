package com.example.KinoXP.controller;

import com.example.KinoXP.dto.MovieRequest;
import com.example.KinoXP.dto.MovieResponse;
import com.example.KinoXP.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/api/movies")
    @ResponseBody
    public MovieResponse createMovie(@RequestBody MovieRequest request) {
        return movieService.createMovie(request);
    }
}
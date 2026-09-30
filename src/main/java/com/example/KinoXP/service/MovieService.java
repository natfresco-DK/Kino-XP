package com.example.KinoXP.service;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.repository.MovieRepo;
import org.springframework.stereotype.Service;

@Service
public class MovieService {

    private final MovieRepo movieRepo;

    public MovieService(MovieRepo movieRepo) {
        this.movieRepo = movieRepo;
    }

    public Movie createMovie(Movie movie) {
        return movieRepo.save(movie);
    }
}
package com.example.KinoXP.dto;

import com.example.KinoXP.model.Movie;
import java.util.List;

public record MovieResponse(
        Long id,
        String title,
        String description,
        List<String> genre,
        long duration,
        List<String> actors,
        int releaseYear,
        Movie.AgeLimit ageLimit
) {
    public static MovieResponse from(Movie movie) {
        return new MovieResponse(
                movie.getId(),
                movie.getTitle(),
                movie.getDescription(),
                movie.getGenre(),
                movie.getDuration().toMinutes(),
                movie.getActors(),
                movie.getReleaseYear(),
                movie.getAgeLimit()
        );
    }
}
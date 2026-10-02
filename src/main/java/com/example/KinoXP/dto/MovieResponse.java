package com.example.KinoXP.dto;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.utils.AgeLimit;

import java.util.List;

public record MovieResponse(
        Long id,
        String title,
        String description,
        List<String> genre,
        Long durationMinutes,
        List<String> actors,
        int releaseYear,
        AgeLimit ageLimit
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
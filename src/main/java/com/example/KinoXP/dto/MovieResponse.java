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
}
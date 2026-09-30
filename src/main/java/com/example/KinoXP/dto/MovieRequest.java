package com.example.KinoXP.dto;

import com.example.KinoXP.model.Movie;
import java.util.List;

public record MovieRequest(
        String title,
        String description,
        List<String> genre,
        Integer duration,
        List<String> actors,
        int releaseYear,
        Movie.AgeLimit ageLimit
) {
}
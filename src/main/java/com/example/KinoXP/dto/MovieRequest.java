package com.example.KinoXP.dto;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.utils.AgeLimit;

import java.time.Duration;
import java.util.List;

public record MovieRequest(
        String title,
        String description,
        List<String> genre,
        Duration duration,
        List<String> actors,
        int releaseYear,
        AgeLimit ageLimit
) {
}
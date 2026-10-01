package com.example.KinoXP;

import com.example.KinoXP.dto.MovieResponse;
import com.example.KinoXP.model.Movie;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieResponseTest {

    @Test
    void from_convertsDurationToMinutes() {
        Movie movie = new Movie(
                1L,
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi"),
                Duration.ofMinutes(169),
                List.of("Matthew McConaughey"),
                2014,
                Movie.AgeLimit.FROM_16
        );

        MovieResponse response = MovieResponse.from(movie);

        assertEquals(1L, response.id());
        assertEquals("Interstellar", response.title());
        assertEquals(169, response.duration());
    }
}
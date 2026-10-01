package com.example.KinoXP;

import com.example.KinoXP.dto.MovieResponse;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.utils.AgeLimit;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieResponseTest {

    @Test
    void from_convertsDurationToMinutes() {
        Movie movie = new Movie(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi"),
                Duration.ofMinutes(169),
                AgeLimit.FROM_16,
                2014,List.of("Matthew McConaughey")
        );

        MovieResponse response = MovieResponse.from(movie);


        assertEquals("Interstellar", response.title());
        assertEquals(169, response.duration());
    }
}
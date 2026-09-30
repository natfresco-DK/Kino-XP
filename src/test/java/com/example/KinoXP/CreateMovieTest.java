package com.example.KinoXP;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.service.MovieService;
import com.example.KinoXP.utils.AgeLimit;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.*;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class CreateMovieTest {

    @Test
    void createMovieTest() {

        MovieRepo movieRepo = mock(MovieRepo.class);
        MovieService movieService = new MovieService(movieRepo);
        List<String> actors = List.of("Actor 1", "Actor 2", "Actor 3");
        Movie movie = new Movie(
                null,
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi", "Sci-Fi-2", "Sci-Fi-3"),
                Duration.ofMinutes(169),
                AgeLimit.FROM_16,
                2000,
                actors
        );

        movieService.createMovie(movie);

        verify(movieRepo).save(movie);
    }
    
}
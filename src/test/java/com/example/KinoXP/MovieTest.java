package com.example.KinoXP;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.service.MovieService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class MovieTest {

    @Test
    void createMovieTest() {

        MovieRepo movieRepo = mock(MovieRepo.class);
        MovieService movieService = new MovieService(movieRepo);

        Movie movie = new Movie(
                null,
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi", "Action"),
                169,
                List.of("Matthew McConaughey", "Anne Hathaway"),
                2014,
                Movie.AgeLimit.AGE_16
        );

        movieService.createMovie(movie);

        verify(movieRepo).save(movie);
    }
}
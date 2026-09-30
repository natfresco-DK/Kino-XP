package com.example.KinoXP;

import com.example.KinoXP.dto.MovieRequest;
import com.example.KinoXP.dto.MovieResponse;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class MovieTest {

    private MovieRepo movieRepo;
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieRepo = mock(MovieRepo.class);
        movieService = new MovieService(movieRepo);

        when(movieRepo.save(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private MovieRequest requestWithDuration(Integer duration) {
        return new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi", "Action"),
                duration,
                List.of("Matthew McConaughey", "Anne Hathaway"),
                2014,
                Movie.AgeLimit.AGE_16
        );
    }

    @Test
    void createMovie_createsMovieWithCorrectData() {

        movieService.createMovie(requestWithDuration(169));

        verify(movieRepo).save(argThat(movie ->
                movie.getTitle().equals("Interstellar") &&
                        movie.getDescription().equals("Space movie") &&
                        movie.getGenre().equals(List.of("Sci-Fi", "Action")) &&
                        movie.getDuration().equals(Duration.ofMinutes(169)) &&
                        movie.getActors().equals(
                                List.of("Matthew McConaughey", "Anne Hathaway")
                        ) &&
                        movie.getReleaseYear() == 2014 &&
                        movie.getAgeLimit() == Movie.AgeLimit.AGE_16
        ));
    }

    @Test
    void createMovie_convertsMinutesToDuration() {

        movieService.createMovie(requestWithDuration(169));

        verify(movieRepo).save(argThat(movie ->
                movie.getDuration().equals(Duration.ofMinutes(169))
        ));
    }

    @Test
    void createMovie_returnsResponseWithMinutes() {

        MovieResponse response =
                movieService.createMovie(requestWithDuration(169));

        assertEquals("Interstellar", response.title());
        assertEquals(169, response.duration());
        assertEquals(Movie.AgeLimit.AGE_16, response.ageLimit());
    }

    @Test
    void createMovie_withoutDuration_throwsBadRequest() {

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> movieService.createMovie(requestWithDuration(null))
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());

        verify(movieRepo, never()).save(any());
    }

    @Test
    void createMovie_withZeroDuration_throwsBadRequest() {

        assertThrows(
                ResponseStatusException.class,
                () -> movieService.createMovie(requestWithDuration(30))
        );

        verify(movieRepo, never()).save(any());
    }

    @Test
    void createMovie_withNegativeDuration_throwsBadRequest() {

        assertThrows(
                ResponseStatusException.class,
                () -> movieService.createMovie(requestWithDuration(-10))
        );

        verify(movieRepo, never()).save(any());
    }
}
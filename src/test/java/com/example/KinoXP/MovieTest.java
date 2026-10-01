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

    @Test
    void createMovie_createsMovieWithCorrectData() {
        MovieRequest request = validRequest();
        movieService.createMovie(request);
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
        movieService.createMovie(validRequest());
        verify(movieRepo).save(argThat(movie ->
                movie.getDuration().equals(Duration.ofMinutes(169))
        ));
    }

    @Test
    void createMovie_returnsResponseWithMinutes() {
        MovieResponse response =
                movieService.createMovie(validRequest());
        assertEquals("Interstellar", response.title());
        assertEquals(169, response.duration());
        assertEquals(Movie.AgeLimit.AGE_16, response.ageLimit());
    }

    @Test
    void createMovie_withoutDuration_throwsBadRequest() {
        MovieRequest request = new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi"),
                null,
                List.of("Matthew McConaughey"),
                2014,
                Movie.AgeLimit.AGE_16
        );

        assertBadRequest(request);
    }

    @Test
    void createMovie_withDuration30_throwsBadRequest() {
        MovieRequest request = new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi"),
                30,
                List.of("Matthew McConaughey"),
                2014,
                Movie.AgeLimit.AGE_16
        );
        assertBadRequest(request);
    }

    @Test
    void createMovie_withNegativeDuration_throwsBadRequest() {
        MovieRequest request = new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi"),
                -10,
                List.of("Matthew McConaughey"),
                2014,
                Movie.AgeLimit.AGE_16
        );
        assertBadRequest(request);
    }

    @Test
    void createMovie_withoutTitle_throwsBadRequest() {

        MovieRequest request = new MovieRequest(
                "",
                "Space movie",
                List.of("Sci-Fi"),
                169,
                List.of("Matthew McConaughey"),
                2014,
                Movie.AgeLimit.AGE_16
        );

        assertBadRequest(request);
    }

    @Test
    void createMovie_withoutDescription_throwsBadRequest() {
        MovieRequest request = new MovieRequest(
                "Interstellar",
                "",
                List.of("Sci-Fi"),
                169,
                List.of("Matthew McConaughey"),
                2014,
                Movie.AgeLimit.AGE_16
        );
        assertBadRequest(request);
    }

    @Test
    void createMovie_withoutGenre_throwsBadRequest() {

        MovieRequest request = new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of(),
                169,
                List.of("Matthew McConaughey"),
                2014,
                Movie.AgeLimit.AGE_16
        );
        assertBadRequest(request);
    }

    @Test
    void createMovie_withoutActors_throwsBadRequest() {

        MovieRequest request = new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi"),
                169,
                List.of(),
                2014,
                Movie.AgeLimit.AGE_16
        );

        assertBadRequest(request);
    }

    @Test
    void createMovie_actorWithNumbers_throwsBadRequest() {
        MovieRequest request = new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi"),
                169,
                List.of("Matthew123"),
                2014,
                Movie.AgeLimit.AGE_16
        );

        assertBadRequest(request);
    }

    @Test
    void createMovie_actorWithInvalidSymbol_throwsBadRequest() {
        MovieRequest request = new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi"),
                169,
                List.of("Matthew!"),
                2014,
                Movie.AgeLimit.AGE_16
        );
        assertBadRequest(request);
    }

    @Test
    void createMovie_withTooOldReleaseYear_throwsBadRequest() {
        MovieRequest request = new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi"),
                169,
                List.of("Matthew McConaughey"),
                1800,
                Movie.AgeLimit.AGE_16
        );
        assertBadRequest(request);
    }

    @Test
    void createMovie_withTooFarFutureReleaseYear_throwsBadRequest() {
        MovieRequest request = new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi"),
                169,
                List.of("Matthew McConaughey"),
                2100,
                Movie.AgeLimit.AGE_16
        );
        assertBadRequest(request);
    }

    @Test
    void createMovie_withoutAgeLimit_throwsBadRequest() {

        MovieRequest request = new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi"),
                169,
                List.of("Matthew McConaughey"),
                2014,
                null
        );

        assertBadRequest(request);
    }

    private MovieRequest validRequest() {
        return new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi", "Action"),
                169,
                List.of("Matthew McConaughey", "Anne Hathaway"),
                2014,
                Movie.AgeLimit.AGE_16
        );
    }

    private void assertBadRequest(MovieRequest request) {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> movieService.createMovie(request)
        );
        assertEquals(
                HttpStatus.BAD_REQUEST,
                exception.getStatusCode()
        );
        verify(movieRepo, never()).save(any());
    }
}
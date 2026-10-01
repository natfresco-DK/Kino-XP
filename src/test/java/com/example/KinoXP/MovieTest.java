package com.example.KinoXP;

import com.example.KinoXP.dto.MovieRequest;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieTest {

    @Mock
    private MovieRepo movieRepo;

    @InjectMocks
    private MovieService movieService;

    @Test
    void createMovie_createsMovieWithCorrectData() {
        saveReturnsSameMovie();

        movieService.createMovie(validRequest());

        verify(movieRepo).save(argThat(movie ->
                movie.getTitle().equals("Interstellar") &&
                        movie.getDescription().equals("Space movie") &&
                        movie.getGenre().equals(List.of("Sci-Fi", "Action")) &&
                        movie.getDuration().equals(Duration.ofMinutes(169)) &&
                        movie.getActors().equals(
                                List.of("Matthew McConaughey", "Anne Hathaway")
                        ) &&
                        movie.getReleaseYear() == 2014 &&
                        movie.getAgeLimit() == Movie.AgeLimit.FROM_16
        ));
    }

    @Test
    void createMovie_convertsMinutesToDuration() {
        saveReturnsSameMovie();

        movieService.createMovie(validRequest());

        verify(movieRepo).save(argThat(movie ->
                movie.getDuration().equals(Duration.ofMinutes(169))
        ));
    }

    @Test
    void createMovie_returnsSavedMovie() {
        saveReturnsSameMovie();

        Movie movie = movieService.createMovie(validRequest());

        assertEquals("Interstellar", movie.getTitle());
        assertEquals(Duration.ofMinutes(169), movie.getDuration());
        assertEquals(Movie.AgeLimit.FROM_16, movie.getAgeLimit());
    }

    @Test
    void createMovie_withoutDuration_throwsBadRequest() {
        assertBadRequest(new MovieRequest("Interstellar", "Space movie", List.of("Sci-Fi"),
                null, List.of("Matthew McConaughey"), 2014, Movie.AgeLimit.FROM_16));
    }

    @Test
    void createMovie_withDuration30_throwsBadRequest() {
        assertBadRequest(new MovieRequest("Interstellar", "Space movie", List.of("Sci-Fi"),
                30, List.of("Matthew McConaughey"), 2014, Movie.AgeLimit.FROM_16));
    }

    @Test
    void createMovie_withNegativeDuration_throwsBadRequest() {
        assertBadRequest(new MovieRequest("Interstellar", "Space movie", List.of("Sci-Fi"),
                -10, List.of("Matthew McConaughey"), 2014, Movie.AgeLimit.FROM_16));
    }

    @Test
    void createMovie_withoutTitle_throwsBadRequest() {
        assertBadRequest(new MovieRequest("", "Space movie", List.of("Sci-Fi"),
                169, List.of("Matthew McConaughey"), 2014, Movie.AgeLimit.FROM_16));
    }

    @Test
    void createMovie_withoutDescription_throwsBadRequest() {
        assertBadRequest(new MovieRequest("Interstellar", "", List.of("Sci-Fi"),
                169, List.of("Matthew McConaughey"), 2014, Movie.AgeLimit.FROM_16));
    }

    @Test
    void createMovie_withoutGenre_throwsBadRequest() {
        assertBadRequest(new MovieRequest("Interstellar", "Space movie", List.of(),
                169, List.of("Matthew McConaughey"), 2014, Movie.AgeLimit.FROM_16));
    }

    @Test
    void createMovie_withoutActors_throwsBadRequest() {
        assertBadRequest(new MovieRequest("Interstellar", "Space movie", List.of("Sci-Fi"),
                169, List.of(), 2014, Movie.AgeLimit.FROM_16));
    }

    @Test
    void createMovie_actorWithNumbers_throwsBadRequest() {
        assertBadRequest(new MovieRequest("Interstellar", "Space movie", List.of("Sci-Fi"),
                169, List.of("Matthew123"), 2014, Movie.AgeLimit.FROM_16));
    }

    @Test
    void createMovie_actorWithInvalidSymbol_throwsBadRequest() {
        assertBadRequest(new MovieRequest("Interstellar", "Space movie", List.of("Sci-Fi"),
                169, List.of("Matthew!"), 2014, Movie.AgeLimit.FROM_16));
    }

    @Test
    void createMovie_withTooOldReleaseYear_throwsBadRequest() {
        assertBadRequest(new MovieRequest("Interstellar", "Space movie", List.of("Sci-Fi"),
                169, List.of("Matthew McConaughey"), 1800, Movie.AgeLimit.FROM_16));
    }

    @Test
    void createMovie_withTooFarFutureReleaseYear_throwsBadRequest() {
        assertBadRequest(new MovieRequest("Interstellar", "Space movie", List.of("Sci-Fi"),
                169, List.of("Matthew McConaughey"), 2100, Movie.AgeLimit.FROM_16));
    }

    @Test
    void createMovie_withoutAgeLimit_throwsBadRequest() {
        assertBadRequest(new MovieRequest("Interstellar", "Space movie", List.of("Sci-Fi"),
                169, List.of("Matthew McConaughey"), 2014, null));
    }

    private MovieRequest validRequest() {
        return new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi", "Action"),
                169,
                List.of("Matthew McConaughey", "Anne Hathaway"),
                2014,
                Movie.AgeLimit.FROM_16
        );
    }


    private void saveReturnsSameMovie() {
        when(movieRepo.save(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private void assertBadRequest(MovieRequest request) {
        assertThrows(
                IllegalArgumentException.class,
                () -> movieService.createMovie(request)
        );
        verify(movieRepo, never()).save(any());
    }
}
package com.example.KinoXP;

import com.example.KinoXP.dto.MovieRequest;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.service.MovieService;
import com.example.KinoXP.utils.AgeLimit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.temporal.*;
import java.util.List;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateMovieTest {

    @Mock
    private MovieRepo movieRepo;

    @InjectMocks
    private MovieService movieService;

    @Test
    void createMovieTest() {
        MovieRequest request = new MovieRequest(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi", "Sci-Fi-2", "Sci-Fi-3"),
                Duration.ofMinutes(167),
                List.of("Matthew McConaughey"),
                2000,
                AgeLimit.FROM_16
        );

        when(movieRepo.save(any(Movie.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        movieService.createMovie(request);

        verify(movieRepo).save(any(Movie.class));
    }
}
package com.example.KinoXP;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.model.Screen;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.repository.ScreeningRepo;
import com.example.KinoXP.service.MovieService;
import com.example.KinoXP.service.ScreeningService;
import com.example.KinoXP.utils.AgeLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;


public class ScreeningTest {

    private Movie movie;
    private Screen screen;

    @BeforeEach
    void setUp() {
        MovieRepo movieRepo = mock(MovieRepo.class);
        MovieService movieService = new MovieService(movieRepo);
        List<String> actors = List.of("Actor 1", "Actor 2", "Actor 3");
        movie = new Movie(
                "Interstellar",
                "Space movie",
                List.of("Sci-Fi", "Sci-Fi-2", "Sci-Fi-3"),
                Duration.ofMinutes(169),
                AgeLimit.FROM_16,
                2000,
                actors
        );
        screen = new Screen("Screen 1");
    }

    @Test
    void createScreeningTest(){
        ScreeningRepo screeningRepo = mock(ScreeningRepo.class);
        ScreeningService screeningService = new ScreeningService(screeningRepo);
        Screening screening = new Screening(movie, screen, LocalDateTime.now(), LocalDateTime.now().plusHours(2));
        screeningService.createScreening(screening);

        verify(screeningRepo).save(screening);

    }
}

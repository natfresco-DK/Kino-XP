package com.example.KinoXP;

import com.example.KinoXP.dto.CreateScreeningRequest;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.model.Screen;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.repository.ScreenRepo;
import com.example.KinoXP.repository.ScreeningRepo;
import com.example.KinoXP.service.ScreeningService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest {

    @Mock
    private MovieRepo movieRepo;

    @Mock
    private ScreenRepo screenRepo;

    @Mock
    private ScreeningRepo screeningRepo;

    @InjectMocks
    private ScreeningService screeningService;


    @Test
    void should_create_screening() {

        // Arrange
        Movie movie = new Movie();
        Screen screen = new Screen("Sal 1");

        CreateScreeningRequest request =
                new CreateScreeningRequest(
                        1L,
                        1L,
                        LocalDateTime.of(2026, 10, 2, 18, 0),
                        LocalDateTime.of(2026, 10, 2, 20, 0)
                );

        when(movieRepo.findById(1L)).thenReturn(Optional.of(movie));

        when(screenRepo.findById(1L)).thenReturn(Optional.of(screen));

        when(screeningRepo.save(any(Screening.class))).thenReturn(new Screening());


        // Act
        Optional<Screening> result = screeningService.createScreening(request);


        // Assert
        assertTrue(result.isPresent());
    }


    @Test
    void should_return_empty_when_movie_does_not_exist() {

        // Arrange
        CreateScreeningRequest request =
                new CreateScreeningRequest(
                        1L,
                        1L,
                        LocalDateTime.of(2026, 10, 2, 18, 0),
                        LocalDateTime.of(2026, 10, 2, 20, 0)
                );

        when(movieRepo.findById(1L)).thenReturn(Optional.empty());


        // Act
        Optional<Screening> result = screeningService.createScreening(request);


        // Assert
        assertTrue(result.isEmpty());
    }


    @Test
    void should_return_empty_when_screen_does_not_exist() {

        // Arrange
        Movie movie = new Movie();

        CreateScreeningRequest request =
                new CreateScreeningRequest(
                        1L,
                        1L,
                        LocalDateTime.of(2026, 10, 2, 18, 0),
                        LocalDateTime.of(2026, 10, 2, 20, 0)
                );

        when(movieRepo.findById(1L)).thenReturn(Optional.of(movie));

        when(screenRepo.findById(1L)).thenReturn(Optional.empty());


        // Act
        Optional<Screening> result = screeningService.createScreening(request);


        // Assert
        assertTrue(result.isEmpty());
    }


    @Test
    void should_throw_error_when_end_time_is_before_start_time() {

        // Arrange
        CreateScreeningRequest request =
                new CreateScreeningRequest(
                        1L,
                        1L,
                        LocalDateTime.of(2026, 10, 2, 20, 0),
                        LocalDateTime.of(2026, 10, 2, 18, 0)
                );


        // Act + Assert
        assertThrows(
                IllegalArgumentException.class, () -> screeningService.createScreening(request)
        );
    }
}
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        Screen screen = new Screen("Sal 1", 20, 12);

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
    void should_throw_exception_when_movie_does_not_exist() {

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
        assertThrows(IllegalArgumentException.class, () -> screeningService.createScreening(request));
    }


    @Test
    void should_throw_exception_when_screen_does_not_exist() {

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

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> screeningService.createScreening(request));

    }


    @Test
    void should_throw_exception_when_end_time_is_before_start_time() {

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

    @Test
    void should_throw_exception_when_screening_overlaps() {

        // Arrange
        Movie movie = new Movie();
        Screen screen = new Screen("Sal 1", 20, 12);

        CreateScreeningRequest request =
                new CreateScreeningRequest(
                        1L,
                        1L,
                        LocalDateTime.of(2026, 10, 2, 19, 0),
                        LocalDateTime.of(2026, 10, 2, 21, 0));
        when(movieRepo.findById(1L)).thenReturn(Optional.of(movie));
        when(screenRepo.findById(1L)).thenReturn(Optional.of(screen));
        when(screeningRepo.isOverlapping(
                screen,
                request.startTime(),
                request.endTime()
        )).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> screeningService.createScreening(request));

        // Verify
        verify(screeningRepo, never()).save(any(Screening.class));
    }

    @Test
    void should_save_screening_when_screening_does_not_overlap() {

        // Arrange
        Movie movie = new Movie();
        Screen screen = new Screen("Sal 1", 20, 12);
        CreateScreeningRequest request =
                new CreateScreeningRequest(
                        1L,
                        1L,
                        LocalDateTime.of(2026, 10, 2, 20, 0),
                        LocalDateTime.of(2026, 10, 2, 22, 0)
                );
        when(movieRepo.findById(1L)).thenReturn(Optional.of(movie));
        when(screenRepo.findById(1L)).thenReturn(Optional.of(screen));

        when(screeningRepo.isOverlapping(
                screen,
                request.startTime(),
                request.endTime()
        )).thenReturn(false);

        Screening savedScreening = new Screening(
                movie,
                screen,
                request.startTime(),
                request.endTime()
        );
        when(screeningRepo.save(any(Screening.class))).thenReturn(savedScreening);

        // Act
        Optional<Screening> result = screeningService.createScreening(request);

        // Assert
        assertTrue(result.isPresent());

        // Verify
        verify(screeningRepo).save(any(Screening.class));
    }

}
package com.example.KinoXP;

import com.example.KinoXP.model.Screening;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.repository.ScreenRepo;
import com.example.KinoXP.repository.ScreeningRepo;
import com.example.KinoXP.service.ScreeningService;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

public class CanselScreeningAndDelete {

    @Test
    void cancelScreeningDeletesScreening() {

        ScreeningRepo screeningRepo = mock(ScreeningRepo.class);
        MovieRepo movieRepo = mock(MovieRepo.class);
        ScreenRepo screenRepo = mock(ScreenRepo.class);

        ScreeningService screeningService =
                new ScreeningService(screeningRepo, movieRepo, screenRepo);

        Screening screening = new Screening();

        when(screeningRepo.findById(1L))
                .thenReturn(Optional.of(screening));

        screeningService.cancelScreening(1L);

        verify(screeningRepo).delete(screening);
    }
}
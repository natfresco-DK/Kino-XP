package com.example.KinoXP;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.model.Screen;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.repository.ScreenRepo;
import com.example.KinoXP.repository.ScreeningRepo;
import com.example.KinoXP.utils.AgeLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ScreeningRepoTest {

    @Autowired private ScreeningRepo screeningRepo;
    @Autowired private MovieRepo movieRepo;
    @Autowired private ScreenRepo screenRepo;

    private Movie interstellar;
    private Movie dune;
    private Screen sal1;
    private Screen sal2;

    private final LocalDateTime now = LocalDate.now().atTime(12, 0);
    private final LocalDateTime day1 = LocalDate.now().plusDays(1).atTime(18, 0);
    private final LocalDateTime day2 = LocalDate.now().plusDays(2).atTime(18, 0);
    private final LocalDateTime day3 = LocalDate.now().plusDays(3).atTime(18, 0);

    @BeforeEach
    void setUp() {
        interstellar = movieRepo.save(newMovie("Interstellar"));
        dune = movieRepo.save(newMovie("Dune"));
        sal1 = screenRepo.save(new Screen("Sal 1"));
        sal2 = screenRepo.save(new Screen("Sal 2"));
    }

    @Test
    void findByMovieId_returnsScreeningsSortedByStartTime() {
        save(interstellar, sal1, day3);
        save(interstellar, sal1, day1);
        save(interstellar, sal1, day2);

        List<Screening> result =
                screeningRepo.findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(interstellar.getId(), now);

        assertEquals(3, result.size());
        assertEquals(day1, result.getFirst().getStartTime());
        assertEquals(day2, result.get(1).getStartTime());
        assertEquals(day3, result.get(2).getStartTime());
    }

    @Test
    void findByMovieId_returnsOnlyScreeningsForThatMovie() {
        save(interstellar, sal1, day1);
        save(dune, sal2, day1);
        save(dune, sal1, day2);

        List<Screening> result =
                screeningRepo.findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(interstellar.getId(), now);

        assertEquals(1, result.size());
        assertEquals("Interstellar", result.getFirst().getMovie().getTitle());
    }

    @Test
    void findByMovieId_doesNotReturnPastScreenings() {
        save(interstellar, sal1, now.minusDays(1));
        save(interstellar, sal1, now.minusHours(2));
        save(interstellar, sal1, day1);

        List<Screening> result =
                screeningRepo.findByMovieIdAndStartTimeAfterOrderByStartTimeAsc(interstellar.getId(), now);

        assertEquals(1, result.size());
        assertEquals(day1, result.getFirst().getStartTime());
    }

    @Test
    void findByDate_returnsOnlyScreeningsOnThatDate() {
        LocalDate date = LocalDate.now().plusDays(5);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        save(interstellar, sal1, start.minusMinutes(1));
        save(dune, sal2, date.atTime(20, 0));
        save(interstellar, sal1, start);
        save(dune, sal1, end);

        List<Screening> result =
                screeningRepo.findByStartTimeGreaterThanEqualAndStartTimeLessThanOrderByStartTimeAsc(start, end);

        assertEquals(2, result.size());
        assertEquals(start, result.getFirst().getStartTime());
        assertEquals(date.atTime(20, 0), result.get(1).getStartTime());
        assertEquals("Sal 2", result.get(1).getScreen().getName());
    }

    private void save(Movie movie, Screen screen, LocalDateTime startTime) {
        screeningRepo.save(new Screening(movie, screen, startTime, startTime.plusHours(3)));
    }

    private Movie newMovie(String title) {
        return new Movie(
                title,
                "Beskrivelse",
                List.of("Sci-Fi"),
                Duration.ofMinutes(150),
                AgeLimit.FROM_16,
                2014,
                List.of("Matthew McConaughey")
        );
    }
}
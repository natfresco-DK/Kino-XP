package com.example.KinoXP;

import com.example.KinoXP.dto.SeatResponse;
import com.example.KinoXP.model.*;
import com.example.KinoXP.repository.*;
import com.example.KinoXP.service.SeatService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.example.KinoXP.utils.ScreenType;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

// Tester med en H2-testdatabase.
@DataJpaTest
@Import(SeatService.class)
class ReservedStatusChangeTest {

    @Autowired private MovieRepo movieRepo;
    @Autowired private ScreenRepo screenRepo;
    @Autowired private ScreeningRepo screeningRepo;
    @Autowired private SeatRepo seatRepo;
    @Autowired private ReservationRepo reservationRepo;
    @Autowired private SeatService seatService;
    @Autowired private EntityManager entityManager;

    @Test
    void show_seat_reserved_after_reservation(){
        //arange: Opret og gem film til en sal1 (lille sal)
        Movie movie = movieRepo.save(new Movie());
        Screen screen = screenRepo.save(new Screen("Sal 1", ScreenType.SMALL));

        LocalDateTime start = LocalDateTime.of(2026, 10, 7, 18, 0);
        Screening screening = screeningRepo.save(
                new Screening(movie, screen, start, start.plusHours(2))
        );

        //Gemmer sæde A1 i salen.
        Seat seat = seatRepo.save(new Seat("A", 1, screen));

        //Henter status på sædet FØR reservation
        SeatResponse before = seatService
                .getSeatsForScreening(screening.getId()) //henter alle sæder + status til forestilling
                .stream() //Gør listen til en stream, så filtrer er muligt.
                .filter(response -> response.id().equals(seat.getId())) //Behold kun testsædet
                .findFirst()
                .orElseThrow();

        //Kontroller at sædet er ledigt (starter false)
        assertFalse(before.reserved(), "Sædet skal være ledigt");

        reservationRepo.saveAndFlush(
                new Reservation(screening, seat, "12345678")
        );

        //Henter status EFTER reservation.
        SeatResponse after = seatService
                .getSeatsForScreening(screening.getId())
                .stream()
                .filter(response -> response.id().equals(seat.getId()))
                .findFirst()
                .orElseThrow();

        //Assert: kontroller at sæde er reseveret til forestilling
        assertTrue(after.reserved(), "Sædet skal være reserveret");
    }
}

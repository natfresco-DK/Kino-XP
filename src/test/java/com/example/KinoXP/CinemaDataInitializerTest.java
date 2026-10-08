package com.example.KinoXP;

import com.example.KinoXP.model.Screen;
import com.example.KinoXP.model.Seat;
import com.example.KinoXP.repository.ScreenRepo;
import com.example.KinoXP.repository.SeatRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CinemaDataInitializerTest {

    @Autowired
    private ScreenRepo screenRepo;

    @Autowired
    private SeatRepo seatRepo;

    @Test
    void initializesSmallScreenAndSeatLayout() {
        Screen screen = screenRepo.findByName("Lille").orElseThrow();
        List<Seat> seats = seatRepo.findByScreen(screen);

        assertEquals(20, screen.getRowCount());
        assertEquals(12, screen.getSeatsPerRow());
        assertEquals(20 * 12, seats.size());

        assertSeatLayout(seats, 20, 12);
    }

    @Test
    void initializesLargeScreenAndSeatLayout() {
        Screen screen = screenRepo.findByName("Stor").orElseThrow();
        List<Seat> seats = seatRepo.findByScreen(screen);

        assertEquals(25, screen.getRowCount());
        assertEquals(16, screen.getSeatsPerRow());
        assertEquals(25 * 16, seats.size());

        assertSeatLayout(seats, 25, 16);
    }

    private void assertSeatLayout(
            List<Seat> seats,
            int expectedRows,
            int expectedSeatsPerRow
    ) {
        Map<String, List<Seat>> seatsByRow = seats.stream()
                .collect(Collectors.groupingBy(Seat::getRow));

        assertEquals(expectedRows, seatsByRow.size());

        for (int rowNumber = 0; rowNumber < expectedRows; rowNumber++) {
            String rowName = String.valueOf((char) ('A' + rowNumber));
            List<Seat> rowSeats = seatsByRow.get(rowName);

            assertNotNull(rowSeats);
            assertEquals(expectedSeatsPerRow, rowSeats.size());

            Map<Integer, Seat> seatsByNumber = rowSeats.stream()
                    .collect(Collectors.toMap(Seat::getSeatNumber, Function.identity()));

            for (int seatNumber = 1; seatNumber <= expectedSeatsPerRow; seatNumber++) {
                assertNotNull(seatsByNumber.get(seatNumber));
                assertEquals(rowName, seatsByNumber.get(seatNumber).getRow());
            }
        }
    }
}
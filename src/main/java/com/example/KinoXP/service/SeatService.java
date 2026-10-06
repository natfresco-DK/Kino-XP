package com.example.KinoXP.service;

import com.example.KinoXP.dto.SeatResponse;
import com.example.KinoXP.model.Reservation;
import com.example.KinoXP.model.Screen;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.model.Seat;
import com.example.KinoXP.repository.ReservationRepo;
import com.example.KinoXP.repository.ScreeningRepo;
import com.example.KinoXP.repository.ScreenRepo;
import com.example.KinoXP.repository.SeatRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {

    private final SeatRepo seatRepo;
    private final ScreeningRepo screeningRepo;
    private final ReservationRepo reservationRepo;
    private final ScreenRepo screenRepo;

    public SeatService(
            SeatRepo seatRepo,
            ScreeningRepo screeningRepo,
            ReservationRepo reservationRepo,
            ScreenRepo screenRepo) {

        this.seatRepo = seatRepo;
        this.screeningRepo = screeningRepo;
        this.reservationRepo = reservationRepo;
        this.screenRepo = screenRepo;
    }


    public List<SeatResponse> getSeatsForScreening(Long screeningId) {

        Screening screening = screeningRepo.findById(screeningId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Screening not found")
                );

        List<Seat> seats =
                seatRepo.findByScreen(screening.getScreen());

        List<Reservation> reservations =
                reservationRepo.findByScreening(screening);

        List<SeatResponse> responses = new ArrayList<>();

        for (Seat seat : seats) {

            boolean reserved = false;

            for (Reservation reservation : reservations) {

                if (reservation.getSeat().getId().equals(seat.getId())) {
                    reserved = true;
                    break;
                }
            }

            responses.add(
                    new SeatResponse(
                            seat.getId(),
                            seat.getRow(),
                            seat.getSeatNumber(),
                            reserved
                    )
            );
        }

        return responses;
    }


    public void createSeatsForScreen(Long screenId) {

        Screen screen = screenRepo.findById(screenId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Screen not found")
                );

        // Tjek om salen allerede har sæder
        List<Seat> existingSeats = seatRepo.findByScreen(screen);

        if (!existingSeats.isEmpty()) {
            return;
        }

        int rows = screen.getScreenType().getRows();
        int seatsPerRow = screen.getScreenType().getSeatsPerRow();

        for (int rowNumber = 0; rowNumber < rows; rowNumber++) {

            String row = String.valueOf((char) ('A' + rowNumber));

            for (int seatNumber = 1; seatNumber <= seatsPerRow; seatNumber++) {

                Seat seat = new Seat(
                        row,
                        seatNumber,
                        screen
                );

                seatRepo.save(seat);
            }
        }
    }
}
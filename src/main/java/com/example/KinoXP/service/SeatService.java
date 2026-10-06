package com.example.KinoXP.service;

import com.example.KinoXP.dto.SeatResponse;
import com.example.KinoXP.model.Reservation;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.model.Seat;
import com.example.KinoXP.repository.ReservationRepo;
import com.example.KinoXP.repository.ScreeningRepo;
import com.example.KinoXP.repository.SeatRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatService {

    private final SeatRepo seatRepo;
    private final ScreeningRepo screeningRepo;
    private final ReservationRepo reservationRepo;

    public SeatService(
            SeatRepo seatRepo,
            ScreeningRepo screeningRepo,
            ReservationRepo reservationRepo) {

        this.seatRepo = seatRepo;
        this.screeningRepo = screeningRepo;
        this.reservationRepo = reservationRepo;
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
}
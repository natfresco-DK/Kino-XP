package com.example.KinoXP.service;

import com.example.KinoXP.dto.CreateReservationRequest;
import com.example.KinoXP.dto.ReservationResponse;
import com.example.KinoXP.model.Reservation;
import com.example.KinoXP.model.Screening;
import com.example.KinoXP.model.Seat;
import com.example.KinoXP.repository.ReservationRepo;
import com.example.KinoXP.repository.ScreeningRepo;
import com.example.KinoXP.repository.SeatRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ReservationService {

    private final ReservationRepo reservationRepo;
    private final ScreeningRepo screeningRepo;
    private final SeatRepo seatRepo;

    public ReservationService(
            ReservationRepo reservationRepo,
            ScreeningRepo screeningRepo,
            SeatRepo seatRepo) {

        this.reservationRepo = reservationRepo;
        this.screeningRepo = screeningRepo;
        this.seatRepo = seatRepo;
    }

    @Transactional
    public List<ReservationResponse> createReservation(
            CreateReservationRequest request) {

        if (request == null) {
            throw new IllegalArgumentException(
                    "Reservationsoplysninger mangler"
            );
        }

        String phoneNumber = validatePhoneNumber(request.phoneNumber());
        List<Long> seatIds = validateSeatIds(request.seatIds());

        Screening screening =
                getUpcomingScreeningOrThrow(request.screeningId());

        List<Seat> seats =
                getSeatsInScreeningOrThrow(seatIds, screening);

        if (reservationRepo.existsByScreeningAndSeatIdIn(screening, seatIds)) {
            throw new IllegalStateException(
                    "Et eller flere af sæderne er allerede reserveret"
            );
        }

        List<Reservation> reservations = seats.stream()
                .map(seat -> new Reservation(screening, seat, phoneNumber))
                .toList();

        return reservationRepo.saveAll(reservations)
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    private String validatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Telefonnummer skal udfyldes");
        }

        String trimmed = phoneNumber.trim();

        if (!trimmed.matches("[0-9]{8}")) {
            throw new IllegalArgumentException(
                    "Telefonnummer skal indeholde præcis 8 cifre og kun tal"
            );
        }

        return trimmed;
    }

    private List<Long> validateSeatIds(List<Long> seatIds) {
        if (seatIds == null
                || seatIds.isEmpty()
                || seatIds.stream().anyMatch(Objects::isNull)) {

            throw new IllegalArgumentException("Vælg mindst ét sæde");
        }

        return seatIds.stream().distinct().toList();
    }

    private Screening getUpcomingScreeningOrThrow(Long screeningId) {
        if (screeningId == null) {
            throw new IllegalArgumentException(
                    "Vælg en forestilling"
            );
        }

        Screening screening = screeningRepo.findById(screeningId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Forestillingen findes ikke"
                        )
                );

        if (!screening.getStartTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException(
                    "Forestillingen er allerede startet"
            );
        }

        return screening;
    }

    private List<Seat> getSeatsInScreeningOrThrow(
            List<Long> seatIds,
            Screening screening) {

        List<Seat> seats = seatRepo.findAllById(seatIds);

        if (seats.size() != seatIds.size()) {
            throw new IllegalArgumentException(
                    "Et eller flere sæder findes ikke"
            );
        }

        Long screenId = screening.getScreen().getId();

        for (Seat seat : seats) {
            if (!seat.getScreen().getId().equals(screenId)) {
                throw new IllegalArgumentException(
                        "Sædet hører ikke til forestillingens sal"
                );
            }
        }

        return seats;
    }
}
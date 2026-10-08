package com.example.KinoXP.dto;
import com.example.KinoXP.model.Reservation;
public record ReservationResponse(
        Long id,
        Long screeningId,
        Long seatId,
        String phoneNumber
) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getScreening().getId(),
                reservation.getSeat().getId(),
                reservation.getPhoneNumber()
        );
    }
}

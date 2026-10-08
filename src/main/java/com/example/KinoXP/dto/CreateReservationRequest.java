package com.example.KinoXP.dto;

import java.util.List;

public record CreateReservationRequest(
        Long screeningId,
        List<Long> seatIds,
        String phoneNumber
) {
}
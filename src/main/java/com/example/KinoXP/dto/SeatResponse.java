package com.example.KinoXP.dto;

public record SeatResponse(
        Long id,
        String row,
        int seatNumber,
        boolean reserved
) {
}
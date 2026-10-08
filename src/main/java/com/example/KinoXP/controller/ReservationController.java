
package com.example.KinoXP.controller;

import com.example.KinoXP.dto.CreateReservationRequest;
import com.example.KinoXP.dto.ReservationResponse;
import com.example.KinoXP.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<List<ReservationResponse>> createReservation(
            @RequestBody CreateReservationRequest request) {

        List<ReservationResponse> reservations =
                reservationService.createReservation(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reservations);
    }
}

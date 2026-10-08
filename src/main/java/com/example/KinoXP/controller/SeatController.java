package com.example.KinoXP.controller;

import com.example.KinoXP.dto.SeatResponse;
import com.example.KinoXP.service.SeatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class SeatController {
  private final SeatService seatService;

  public SeatController(SeatService seatService){
      this.seatService = seatService;
  }

    @GetMapping("/screenings/{screeningId}/seats")
    public List<SeatResponse> getSeatsForScreening(
            @PathVariable Long screeningId) {

        return seatService.getSeatsForScreening(screeningId);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(IllegalArgumentException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("message", e.getMessage()));
    }
}

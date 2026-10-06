package com.example.KinoXP.controller;

import com.example.KinoXP.dto.SeatResponse;
import com.example.KinoXP.service.SeatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}

package com.example.KinoXP.repository;

import com.example.KinoXP.model.Seat;
import com.example.KinoXP.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepo extends JpaRepository<Seat, Long> {

    List<Seat> findByScreen(Screen screen);
}
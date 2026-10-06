package com.example.KinoXP.repository;

import com.example.KinoXP.model.Reservation;
import com.example.KinoXP.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    List<Reservation> findByScreening(Screening screening);
}
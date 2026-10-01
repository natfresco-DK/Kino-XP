package com.example.KinoXP.repository;
import com.example.KinoXP.model.Movie;
import com.example.KinoXP.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreeningRepo extends JpaRepository<Screening,Long> {
}

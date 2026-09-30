package com.example.KinoXP.repository;

import com.example.KinoXP.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenRepo extends JpaRepository<Screen, Long> {
}
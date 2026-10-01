package com.example.KinoXP.repository;

import com.example.KinoXP.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepo extends JpaRepository<Movie, Long> {
}

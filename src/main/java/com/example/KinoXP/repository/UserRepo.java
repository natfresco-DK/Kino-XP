package com.example.KinoXP.repository;

import com.example.KinoXP.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepo extends JpaRepository<User, Long> {
}

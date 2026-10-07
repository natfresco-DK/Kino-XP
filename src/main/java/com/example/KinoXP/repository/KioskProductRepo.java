package com.example.KinoXP.repository;

import com.example.KinoXP.model.KioskProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KioskProductRepo extends JpaRepository<KioskProduct, Long> {

    boolean existsByProductIgnoreCase(String product);
}
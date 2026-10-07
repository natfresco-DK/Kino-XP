package com.example.KinoXP.repository;

import com.example.KinoXP.model.KioskSale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KioskSaleRepo extends JpaRepository<KioskSale, Long> {

    List<KioskSale> findAllByOrderBySaleTimeDesc();
}
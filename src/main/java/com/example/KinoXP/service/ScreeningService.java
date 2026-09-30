package com.example.KinoXP.service;

import com.example.KinoXP.model.Screening;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.repository.ScreeningRepo;

public class ScreeningService {

    private final ScreeningRepo ScreeningRepo;

    public ScreeningService(ScreeningRepo screeningRepo) {
        this.ScreeningRepo = screeningRepo;
    }
    public Screening createScreening(Screening screening){
        return ScreeningRepo.save(screening);
    }
}

package com.example.KinoXP.service;

import com.example.KinoXP.model.Screening;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.repository.ScreeningRepo;

public class ScreeningService {

    private final ScreeningRepo ScreeningRepo;
    private final MovieRepo MovieRepo;

    public ScreeningService(ScreeningRepo screeningRepo, MovieRepo movieRepo) {
        this.ScreeningRepo = screeningRepo;
        this.MovieRepo = movieRepo;
    }
    public Screening createScreening(Screening screening){
        return ScreeningRepo.save(screening);
    }
}

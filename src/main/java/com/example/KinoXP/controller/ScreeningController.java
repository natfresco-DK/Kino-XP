package com.example.KinoXP.controller;

import com.example.KinoXP.model.Screening;
import com.example.KinoXP.service.ScreeningService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class ScreeningController {
    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping("/screenings/create")
    public String showCreateScreeninForm(){
        return "CreateScreening";
    }

    @PostMapping("/screenings")
    public Screening createScreening(@RequestBody Screening screening){
        return screeningService.createScreening(screening);
    }
}

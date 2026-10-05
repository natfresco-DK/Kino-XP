package com.example.KinoXP.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ScreeningPageController {

    @GetMapping("/screenings/create")
    public String showCreateScreeninForm() {
        return "screening/create";
    }

    @GetMapping("/program")
    public String showProgram() {
        return "Program";
    }
}

package com.example.KinoXP.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoviePageController {


    @GetMapping("/movies")
    public String movies() {
        return "movies";
    }
}
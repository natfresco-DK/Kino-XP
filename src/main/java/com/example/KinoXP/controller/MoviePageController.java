package com.example.KinoXP.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoviePageController {

    @GetMapping("/movies/edit")
    public String moviesPage() {
        return "movies/edit";
    }

    @GetMapping("/movies/create")
    public String moviesCreatePage() {
        return "movies/create";
    }

}
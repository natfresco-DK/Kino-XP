package com.example.KinoXP.controller;

import com.example.KinoXP.model.Film;
import com.example.KinoXP.service.FilmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FilmController {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/api/films")
    public Film create(@RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @GetMapping("/api/films")
    public List<Film> getAllFilms(){
        return filmService.getAllFilms();
    }

}

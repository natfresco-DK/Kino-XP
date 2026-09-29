package com.example.KinoXP.service;

import com.example.KinoXP.model.Film;
import com.example.KinoXP.repository.FilmRepo;
import org.springframework.stereotype.Service;

@Service
public class FilmService {

    private final FilmRepo filmRepo;

    public FilmService(FilmRepo filmRepo) {
        this.filmRepo = filmRepo;
    }

    public Film createFilm(Film film) {
        return filmRepo.save(film);
    }
}
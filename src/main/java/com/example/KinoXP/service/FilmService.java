package com.example.KinoXP.service;

import com.example.KinoXP.model.Film;
import com.example.KinoXP.repository.FilmRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService {

    private final FilmRepo filmRepo;

    public FilmService(FilmRepo filmRepo) {
        this.filmRepo = filmRepo;
    }

    public Film createFilm(Film film) {
        return filmRepo.save(film);
    }

    public List<Film> getAllFilms(){
        return filmRepo.findAll();
    }

}
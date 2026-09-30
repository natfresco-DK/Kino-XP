package com.example.KinoXP;

import com.example.KinoXP.model.Film;
import com.example.KinoXP.repository.FilmRepo;
import com.example.KinoXP.service.FilmService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FilmTest {

    @Test
    void createFilmTest() {

        FilmRepo filmRepo = mock(FilmRepo.class);
        FilmService filmService = new FilmService(filmRepo);

        Film film = new Film(
                null,
                "Interstellar",
                "Space movie",
                "Sci-Fi",
                "169 min",
                11,
                "Christopher Nolan",
                "2014-11-07"
        );

        filmService.createFilm(film);

        verify(filmRepo).save(film);
    }


    @Test
    void getAllFilmsTest() {

        FilmRepo filmRepo = mock(FilmRepo.class);
        FilmService filmService = new FilmService(filmRepo);

        filmService.getAllFilms();

        verify(filmRepo).findAll();
    }
    
}
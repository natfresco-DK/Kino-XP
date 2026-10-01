package com.example.KinoXP;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.service.MovieService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class FilmTest {

    @Test
    void createFilmTest() {

        MovieRepo filmRepo = mock(MovieRepo.class);
        MovieService filmService = new MovieService(filmRepo);

        Movie film = new Movie(
                null,
                "Interstellar",
                "Space movie",
                "Sci-Fi",
                "169 min",
                11,
                "Christopher Nolan",
                "2014-11-07"
        );

        filmService.createMovie(film);

        verify(filmRepo).save(film);
    }


    @Test
    void getAllFilmsTest() {

        MovieRepo filmRepo = mock(MovieRepo.class);
        MovieService filmService = new MovieService(filmRepo);

        filmService.getAllMovies();

        verify(filmRepo).findAll();
    }
    
}
package com.example.KinoXP;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.service.MovieService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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

    @Test
    void updateMovieNotFound(){
        //Arange
        MovieRepo movieRepo = mock(MovieRepo.class);
        MovieService movieService = new MovieService(movieRepo);
        when(movieRepo.findById(999L)).thenReturn(Optional.empty());

        //Act
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,()
                -> movieService.updateMovie(999L, new Movie()));

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
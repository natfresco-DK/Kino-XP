package com.example.KinoXP;

import com.example.KinoXP.dto.MovieRequest;
import com.example.KinoXP.repository.MovieRepo;
import com.example.KinoXP.service.MovieService;
import com.example.KinoXP.utils.AgeLimit;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class FilmTest {

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
                -> movieService.updateMovie(999L, new MovieRequest(
                "Interstellar", "Space movie", List.of("Sci-Fi"), 169,
                List.of("Matthew McConaughey"), 2014, AgeLimit.FROM_16)));

        //Assert
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
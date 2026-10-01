package com.example.KinoXP.service;

import com.example.KinoXP.model.Movie;
import com.example.KinoXP.repository.MovieRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepo movieRepo;

    public MovieService(MovieRepo movieRepo) {
        this.movieRepo = movieRepo;
    }

    public Movie createMovie(Movie movie) {
        validate(movie);
        return movieRepo.save(movie);
    }

    public List<Movie> getAllMovies(){
        return movieRepo.findAll();
    }

    public Movie updateMovie(Long id, Movie updatedMovie){
        validate(updatedMovie);
        Movie movie = movieRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Film ikke fundet"));

        movie.setTitle(updatedMovie.getTitle());
        movie.setDescription(updatedMovie.getDescription());
        movie.setGenre(updatedMovie.getGenre());
        movie.setDuration(updatedMovie.getDuration());
        movie.setAgeLimit(updatedMovie.getAgeLimit());
        movie.setDirector(updatedMovie.getDirector());


        return movieRepo.save(movie);
    }

    private void validate(Movie movie){

        if (movie.getTitle() == null || movie.getTitle().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Titel må ikke være tom");
        }
        if (movie.getDuration() == null || movie.getDuration().isNegative() || movie.getDuration().isZero()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Varighed skal være større end 0");
        }
        if (movie.getAgeLimit() < 16 || movie.getAgeLimit() > 18){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Aldersgrænse skal være mellem 16 eller 18");
        }

    }

}
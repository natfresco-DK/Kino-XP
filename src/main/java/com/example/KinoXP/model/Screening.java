package com.example.KinoXP.model;

import jakarta.persistence.*;
import com.example.KinoXP.model.Movie;
import java.time.LocalDateTime;

@Entity
@Table(name="screening")
public class Screening {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Movie movie;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Screening(){}

    public Screening(Movie movie, LocalDateTime startTime, LocalDateTime endTime) {
        this.movie = movie;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}

package com.example.KinoXP.model;

import com.example.KinoXP.utils.AgeLimit;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.Duration;
import java.util.List;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private List<String> genre;
    private Duration duration;
    private Enum<AgeLimit> ageLimit;
    private int releaseYear;
    private List<String> actors;

    public Movie(){}

    public Movie(Long id, String title, String description, List<String> genre, Duration duration, Enum<AgeLimit> ageLimit, int releaseYear, List<String> actors) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.ageLimit = ageLimit;
        this.releaseYear = releaseYear;
        this.actors = actors;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getGenre() {
        return genre;
    }

    public Duration getDuration() {
        return duration;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public List<String> getActors() {
        return actors;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGenre(List<String> genre) {
        this.genre = genre;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setAgeLimit(Enum<AgeLimit> ageLimit) {
        this.ageLimit = ageLimit;
    }

    public void  setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
}
package com.example.KinoXP.model;

import com.example.KinoXP.utils.AgeLimit;
import jakarta.persistence.*;

import java.time.Duration;
import java.util.List;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @ElementCollection
    private List<String> genre;
    private Duration duration;
    @Enumerated(EnumType.STRING)
    private AgeLimit ageLimit;
    private int releaseYear;
    @ElementCollection
    private List<String> actors;

    public Movie(){}

    public Movie(String title, String description, List<String> genre, Duration duration, AgeLimit ageLimit, int releaseYear, List<String> actors) {
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

    public void setAgeLimit(AgeLimit ageLimit) {
        this.ageLimit = ageLimit;
    }

    public void  setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }
}
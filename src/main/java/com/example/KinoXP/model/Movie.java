package com.example.KinoXP.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private String genre;
    private String duration;
    private int ageLimit;
    private String director;
    private String premierDay;

    public Movie(){}

    public Movie(Long id, String title, String description, String genre, String duration, int ageLimit, String director, String premierDay) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.duration = duration;
        this.ageLimit = ageLimit;
        this.director = director;
        this.premierDay = premierDay;
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

    public String getGenre() {
        return genre;
    }

    public String getDuration() {
        return duration;
    }

    public int getAgeLimit() {
        return ageLimit;
    }

    public String getDirector() {
        return director;
    }

    public String getPremierDay() {
        return premierDay;
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

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setAgeLimit(int ageLimit) {
        this.ageLimit = ageLimit;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setPremierDay(String premierDay) {
        this.premierDay = premierDay;
    }
}
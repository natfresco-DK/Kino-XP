package com.example.KinoXP.model;

import jakarta.persistence.*;

@Entity
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private int rowCount;
    private int seatsPerRow;

    public Screen() {}

    public Screen(String name, int rowCount, int seatsPerRow) {
        this.name = name;
        this.rowCount = rowCount;
        this.seatsPerRow = seatsPerRow;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getSeatsPerRow() {
        return seatsPerRow;
    }
}
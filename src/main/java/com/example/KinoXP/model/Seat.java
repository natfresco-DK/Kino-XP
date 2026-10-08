package com.example.KinoXP.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "seat",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"screen_id", "row_name", "seat_number"}
        )
)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_name")
    private String row;

    @Column(name = "seat_number")
    private int seatNumber;

    @ManyToOne
    @JoinColumn(name = "screen_id", nullable = false)
    private Screen screen;

    public Seat() {
    }

    public Seat(String row, int seatNumber, Screen screen) {
        this.row = row;
        this.seatNumber = seatNumber;
        this.screen = screen;
    }

    public Long getId() {
        return id;
    }

    public String getRow() {
        return row;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public Screen getScreen() {
        return screen;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
}
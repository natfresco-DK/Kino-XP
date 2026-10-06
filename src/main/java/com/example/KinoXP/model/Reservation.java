package com.example.KinoXP.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "reservation",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"screening_id", "seat_id"}
        )
)
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    public Reservation() {
    }

    public Reservation(Screening screening, Seat seat) {
        this.screening = screening;
        this.seat = seat;
    }

    public Long getId() {
        return id;
    }

    public Screening getScreening() {
        return screening;
    }

    public Seat getSeat() {
        return seat;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
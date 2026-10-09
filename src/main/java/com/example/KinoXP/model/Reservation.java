package com.example.KinoXP.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @Column(nullable = false, length = 8)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Reservation() {
    }

    public Reservation(Screening screening, Seat seat, String phoneNumber) {
        this.screening = screening;
        this.seat = seat;
        this.phoneNumber = phoneNumber;
        this.createdAt = LocalDateTime.now();
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setScreening(Screening screening) {
        this.screening = screening;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
}
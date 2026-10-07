package com.example.KinoXP.model;

import com.example.KinoXP.utils.ScreenType;
import jakarta.persistence.*;

@Entity
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ScreenType screenType;

    public Screen() {}

    public Screen(String name, ScreenType screenType) {
        this.name = name;
        this.screenType = screenType;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ScreenType getScreenType() {
        return screenType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScreenType(ScreenType screenType) {
        this.screenType = screenType;
    }
}
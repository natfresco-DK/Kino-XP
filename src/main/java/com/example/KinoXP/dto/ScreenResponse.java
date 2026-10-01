package com.example.KinoXP.dto;

import com.example.KinoXP.model.Screen;

public record ScreenResponse(
        Long id,
        String name
) {

    public static ScreenResponse from(Screen screen) {
        return new ScreenResponse(
                screen.getId(),
                screen.getName()
        );
    }
}
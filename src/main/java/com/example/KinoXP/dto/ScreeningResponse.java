package com.example.KinoXP.dto;

import com.example.KinoXP.model.Screening;

import java.time.LocalDateTime;

public record ScreeningResponse(
        Long id,
        Long movieId,
        String movieTitle,
        String screenName,
        LocalDateTime startTime,
        LocalDateTime endTime
) {public static ScreeningResponse from(Screening screening) {
        return new ScreeningResponse(
                screening.getId(),
                screening.getMovie().getId(),
                screening.getMovie().getTitle(),
                screening.getScreen().getName(),
                screening.getStartTime(),
                screening.getEndTime()
        );
    }
}
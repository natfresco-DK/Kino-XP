package com.example.KinoXP.dto;

import java.time.LocalDateTime;

public record CreateScreeningRequest(
        Long movieId,
        Long screenId,
        LocalDateTime startTime,
        LocalDateTime endTime
) {}
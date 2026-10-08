package com.example.KinoXP.utils;

import java.util.Arrays;
import java.util.Optional;

public enum UserRole {
    RESERVATION,
    MOVIE_OPERATOR,
    ADMIN;

    public static Optional<UserRole> fromAuthority(String authority) {
        if (authority == null || !authority.startsWith("ROLE_")) {
            return Optional.empty();
        }

        String roleName = authority.substring("ROLE_".length());
        return Arrays.stream(values())
                .filter(role -> role.name().equals(roleName))
                .findFirst();
    }

    public String displayName() {
        return switch (this) {
            case ADMIN -> "Admin";
            case MOVIE_OPERATOR -> "Movie operator";
            case RESERVATION -> "Reservation";
        };
    }
}

package com.example.KinoXP.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class WebModelAttributes {

    @ModelAttribute("currentRole")
    public String currentRole(Authentication authentication) {
        if (authentication == null) {
            return "";
        }

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(this::displayRole)
                .findFirst()
                .orElse("");
    }

    private String displayRole(String authority) {
        return switch (authority) {
            case "ROLE_ADMIN" -> "Admin";
            case "ROLE_MOVIE_OPERATOR" -> "Movie operator";
            case "ROLE_RESERVATION" -> "Reservation";
            default -> "";
        };
    }
}

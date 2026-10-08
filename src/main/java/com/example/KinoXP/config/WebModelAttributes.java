package com.example.KinoXP.config;

import com.example.KinoXP.utils.UserRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

@ControllerAdvice(annotations = Controller.class)
public class WebModelAttributes {

    @ModelAttribute("currentRole")
    public String currentRole(Authentication authentication) {
        if (authentication == null) {
            return "";
        }

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(UserRole::fromAuthority)
                .flatMap(Optional::stream)
                .map(UserRole::displayName)
                .findFirst()
                .orElse("");
    }
}

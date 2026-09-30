package com.example.KinoXP.controller;

import com.example.KinoXP.dto.ScreenResponse;
import com.example.KinoXP.service.ScreenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ScreenController {

    private final ScreenService screenService;

    public ScreenController(ScreenService screenService) {
        this.screenService = screenService;
    }

    @GetMapping("/screens")
    public ResponseEntity<List<ScreenResponse>> getAllScreens() {

        List<ScreenResponse> screens =
                screenService.getAllScreens();

        return ResponseEntity.ok(screens);
    }
}
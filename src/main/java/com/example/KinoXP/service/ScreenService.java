package com.example.KinoXP.service;

import com.example.KinoXP.dto.ScreenResponse;
import com.example.KinoXP.model.Screen;
import com.example.KinoXP.repository.ScreenRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScreenService {

    private final ScreenRepo screenRepo;

    public ScreenService(ScreenRepo screenRepo) {
        this.screenRepo = screenRepo;
    }

    public List<ScreenResponse> getAllScreens() {

        List<Screen> screens = screenRepo.findAll();

        List<ScreenResponse> responses = new ArrayList<>();

        for (Screen screen : screens) {
            responses.add(ScreenResponse.from(screen));
        }

        return responses;
    }
}
package com.example.KinoXP.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class KioskPageController {

    @GetMapping("/kiosk")
    public String showKiosk() {
        return "Kiosk";
    }
}
package com.example.KinoXP.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

@Controller
public class HomePageController {

    @GetMapping({"/", "/index"})
    public String home(Authentication authentication, Model model) {
        model.addAttribute("loggedIn",
                authentication != null
                        && !(authentication instanceof AnonymousAuthenticationToken));
        return "index";
    }

}

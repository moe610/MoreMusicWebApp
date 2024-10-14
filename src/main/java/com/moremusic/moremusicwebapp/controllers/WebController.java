package com.moremusic.moremusicwebapp.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    private static final Logger logger = LoggerFactory.getLogger(WebController.class);

    @GetMapping("/")
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        return "register";
    }

    @GetMapping("/music")
    public String musicPage(){
        logger.info("Serving music page.");
        return "musicPage";
    }

    @GetMapping("/upload")
    public String uploadPage(){
        return "uploadMusicPage";
    }
}

package com.moremusic.moremusicwebapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/")
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/music")
    public String musicPage(){
        return "musicPage";
    }

    @GetMapping("/upload")
    public String uploadPage(){
        return "uploadMusicPage";
    }
}

package com.moremusic.moremusicwebapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {
    @GetMapping("/home")
    public String index(Model model) {
        model.addAttribute("message", "Welcome to MoreMusic Web App!");
        return "index";
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

package com.moremusic.moremusicwebapp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ReactController {

    @GetMapping("/{path:[^\\.]*}") // Matches any path without a dot (e.g., no file extension)
    public String forward() {
        return "forward:/index.html";
    }
}
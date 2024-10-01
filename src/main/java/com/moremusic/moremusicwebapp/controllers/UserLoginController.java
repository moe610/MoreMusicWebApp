package com.moremusic.moremusicwebapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/login")
public class UserLoginController {
    @GetMapping
    public ResponseEntity<String> login() {
        return ResponseEntity.ok("Hello from secure endpoint");
    }
}

package com.moremusic.moremusicwebapp.controllers;

import com.moremusic.moremusicwebapp.datalayer.AuthenticationRequest;
import com.moremusic.moremusicwebapp.datalayer.AuthenticationResponse;
import com.moremusic.moremusicwebapp.datalayer.RegisterRequest;
import com.moremusic.moremusicwebapp.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> Register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.Register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> Authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.Authenticate(request));
    }

    @GetMapping("/validate")
    public ResponseEntity<ResponseEntity<String>> Validate(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok(authenticationService.Validate(authorizationHeader));
    }
}

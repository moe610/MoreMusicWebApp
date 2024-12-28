package com.moremusic.moremusicwebapp.controllers;

import com.moremusic.moremusicwebapp.datalayer.AuthenticationRequest;
import com.moremusic.moremusicwebapp.datalayer.AuthenticationResponse;
import com.moremusic.moremusicwebapp.datalayer.PasswordResetRequest;
import com.moremusic.moremusicwebapp.datalayer.RegisterRequest;
import com.moremusic.moremusicwebapp.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;


    @PostMapping("/register")
    public ResponseEntity<String> Register(@RequestBody RegisterRequest request) throws Exception {
        try{
            String successMessage = authenticationService.Register(request);
            return ResponseEntity.ok(successMessage);
        } catch (IOException e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR) // Server error status
                    .body("An unexpected error occurred. Please try again later.");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Error status
                    .body(e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> Authenticate(@RequestBody AuthenticationRequest request) throws Exception {
        try{
            AuthenticationResponse response = authenticationService.Authenticate(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Error status
                    .body(new AuthenticationResponse(null, e.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<ResponseEntity<String>> Validate(@RequestHeader("Authorization") String authorizationHeader){
        return ResponseEntity.ok(authenticationService.Validate(authorizationHeader));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) throws Exception {
        try{
            String successMessage = authenticationService.resetPassword(request);
            return ResponseEntity.ok(successMessage);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Error status
                    .body(e.getMessage());
        }
    }
}

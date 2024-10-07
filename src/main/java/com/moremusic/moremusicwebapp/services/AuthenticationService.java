package com.moremusic.moremusicwebapp.services;

import com.moremusic.moremusicwebapp.datalayer.AuthenticationRequest;
import com.moremusic.moremusicwebapp.datalayer.AuthenticationResponse;
import com.moremusic.moremusicwebapp.datalayer.RegisterRequest;
import com.moremusic.moremusicwebapp.datalayer.entities.ApplicationUser;
import com.moremusic.moremusicwebapp.datalayer.enums.ApplicationUserRole;
import com.moremusic.moremusicwebapp.datalayer.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse Register(RegisterRequest request) {
        var user = ApplicationUser.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .username(request.getUserName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .applicationUserRole(ApplicationUserRole.USER)
                .build();
        applicationUserRepository.save(user);
        var jwtToken = jwtService.GenerateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse Authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );

        var user = applicationUserRepository.findByUsername(request.getUserName()).orElseThrow();
        var jwtToken = jwtService.GenerateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public ResponseEntity<String> Validate(String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        final String userName = jwtService.ExtractUserName(token);
        UserDetails userDetails = applicationUserRepository.findByUsername(userName).orElseThrow();

        if(jwtService.isTokenValid(token, userDetails)) {
            return ResponseEntity.ok().body(jwtService.ExtractUserName(token));
        } else {
            return ResponseEntity.status(403).body("Invalid Token");
        }
    }
}

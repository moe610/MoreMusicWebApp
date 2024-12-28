package com.moremusic.moremusicwebapp.services;

import com.moremusic.moremusicwebapp.datalayer.AuthenticationRequest;
import com.moremusic.moremusicwebapp.datalayer.AuthenticationResponse;
import com.moremusic.moremusicwebapp.datalayer.PasswordResetRequest;
import com.moremusic.moremusicwebapp.datalayer.RegisterRequest;
import com.moremusic.moremusicwebapp.datalayer.entities.ApplicationUser;
import com.moremusic.moremusicwebapp.datalayer.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final ApplicationUserRepository applicationUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public String Register(RegisterRequest request) throws Exception{
        try {
            String result = "";

            if (applicationUserRepository.findByUsername(request.getUserName().toUpperCase()).isPresent()) {
                throw new Exception("User name has already been used");
            }

            if (applicationUserRepository.getApplicationUserByEmail(request.getEmail()).isPresent()) {
                throw new Exception("Email address has already been used");
            }

            if (!isStrongPassword(request.getPassword())) {
                throw new Exception("Password must have at least one uppercase letter, one lowercase letter, one digit, one special character, and be between 8 - 16 characters");
            }

            var user = ApplicationUser.builder()
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .username(request.getUserName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            result = emailService.sendRegistrationEmail(user);
            applicationUserRepository.save(user);

            return  result;
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public AuthenticationResponse Authenticate(AuthenticationRequest request) throws Exception {
        try{
            var user = applicationUserRepository.getApplicationUserByUsername(request.getUserName().toUpperCase())
                    .orElseThrow(() -> new Exception("User name does not exist."));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getUsername(),
                            request.getPassword()
                    )
            );

            var jwtToken = jwtService.GenerateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
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

    public String resetPassword(PasswordResetRequest request) throws Exception {
        String result = "";
        try{
            if (!request.getPassword().equals(request.getConfirmPassword())){
                throw new Exception("Passwords do not match!");
            }

            if (!createNewPassword(request)){
                throw new Exception("Password has not been reset");
            }

            result = "Password has been reset!";
            return result;
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    public boolean createNewPassword(PasswordResetRequest request) throws Exception {
        boolean success = false;
        try{
            ApplicationUser user = applicationUserRepository.findByResetToken(request.getResetToken())
                    .orElseThrow(() -> new Exception("User does not exist."));

            if (!isStrongPassword(request.getPassword())) {
                throw new Exception("Password must have at least one uppercase letter, one lowercase letter, one digit, one special character, and be between 8 - 16 characters");
            }

            user.setPassword(passwordEncoder.encode(request.getPassword()));
            applicationUserRepository.save(user);

            success = true;
            return success;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private static boolean isStrongPassword(String password){
        if(password.length() < 8 || password.length() > 16){
            return false;
        }

        // Regular expression to check for at least one uppercase letter, one lowercase letter, one digit, and one special character
        String regex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$";

        // Check if the password matches the regex
        return Pattern.matches(regex, password);
    }
}

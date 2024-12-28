package com.moremusic.moremusicwebapp.services;

import com.moremusic.moremusicwebapp.datalayer.ResetRequest;
import com.moremusic.moremusicwebapp.datalayer.entities.ApplicationUser;
import com.moremusic.moremusicwebapp.datalayer.enums.ApplicationUserRole;
import com.moremusic.moremusicwebapp.datalayer.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {
    private final ApplicationUserRepository applicationUserRepository;
    private final EmailService emailService;

    private static final long TOKEN_EXPIRATION_TIME_MILLIS = 3600000; // 1 hour

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return applicationUserRepository.findByUsername(userName)
                .orElseThrow(() -> new UsernameNotFoundException(userName));
    }

    public ApplicationUser getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        return applicationUserRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<ApplicationUser> getAllUsers() {
        //Retrieves all user that have been successfully registers (userRole = USER)
        return applicationUserRepository.getApplicationUsers();
    }

    public String acceptUserRegistration(String userName) {
        try{
            ApplicationUser user = applicationUserRepository.getApplicationUserByUsername(userName.toUpperCase()).orElse(null);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            user.setApplicationUserRole(ApplicationUserRole.USER);
            applicationUserRepository.save(user);

            emailService.sendSuccessfulRegistrationEmail(user);

            return "User registration successful";
        } catch (Exception e){
            throw new UsernameNotFoundException(e.getMessage());
        }
    }

    public String resetPasswordRequest(ResetRequest request) throws Exception {
        try{
            ApplicationUser user;
            if (request == null){
                throw new Exception("Please enter either a your username or email address");
            }
            if (!request.getUserName().isEmpty()){
                user = applicationUserRepository.getApplicationUserByUsername(request.getUserName().toUpperCase())
                        .orElseThrow(() -> new Exception("User name does not exist."));
            } else{
                user = applicationUserRepository.getApplicationUserByEmail(request.getEmail().toUpperCase())
                        .orElseThrow(() -> new Exception("Email address does not exist."));
            }

            String resetToken = generateResetToken();
            user.setResetToken(resetToken);
            applicationUserRepository.save(user);
            emailService.sendResetPasswordEmail(resetToken, user);
            return "Reset password email has been sent.";
        } catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    private String generateResetToken() {
        String uuid = UUID.randomUUID().toString();
        long timestamp = Instant.now().toEpochMilli(); // Current time in milliseconds
        String rawToken = uuid + ":" + timestamp; // Combine UUID and timestamp
        return Base64.getUrlEncoder().withoutPadding().encodeToString(rawToken.getBytes()); // Encode in Base64
    }

    public boolean isResetTokenExpired(String token) {
        String decodedToken = new String(Base64.getUrlDecoder().decode(token));
        String[] parts = decodedToken.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid token format");
        }

        long timestamp = Long.parseLong(parts[1]);
        long currentTimestamp = Instant.now().toEpochMilli();
        return currentTimestamp - timestamp > TOKEN_EXPIRATION_TIME_MILLIS;
    }
}

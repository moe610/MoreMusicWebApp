package com.moremusic.moremusicwebapp.controllers;

import com.moremusic.moremusicwebapp.datalayer.ResetRequest;
import com.moremusic.moremusicwebapp.datalayer.entities.ApplicationUser;
import com.moremusic.moremusicwebapp.services.ApplicationUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/applicationUsers")
public class ApplicationUserController {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationUserController.class);

    @Autowired
    private ApplicationUserService applicationUserService;
    @Value("${application.link}")
    private String applicationLink;

    @GetMapping("/userNames")
    public List<ApplicationUser> getApplicationUsers(){
        logger.info("Retrieving list of users playlists.");
        try{
            return applicationUserService.getAllUsers();
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/currentUser")
    public ApplicationUser getCurrentUser(){
        return applicationUserService.getCurrentUser();
    }

    @GetMapping("/acceptRegistration/{userName}")
    public String acceptRegistration(@PathVariable String userName){
        return applicationUserService.acceptUserRegistration(userName);
    }

    @PostMapping("/resetPasswordRequest")
    public ResponseEntity<String> resetPassword(@RequestBody ResetRequest request) throws Exception {
        try{
            String successMessage = applicationUserService.resetPasswordRequest(request);
            return ResponseEntity.ok(successMessage);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // Error status
                    .body(e.getMessage());
        }
    }

    //Link sent to use to reset password will redirect to React webpage
    @GetMapping("/resetPassword/{resetToken}")
    public void userPasswordReset(@PathVariable String resetToken, HttpServletResponse httpServletResponse) throws Exception {
        boolean isExpired = applicationUserService.isResetTokenExpired(resetToken);
        if (isExpired){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired token");
        }

        httpServletResponse.sendRedirect(applicationLink + "/ResetPassword?token=" + resetToken);
    }
}

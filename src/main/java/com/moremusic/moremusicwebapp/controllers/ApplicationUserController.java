package com.moremusic.moremusicwebapp.controllers;

import com.moremusic.moremusicwebapp.datalayer.entities.ApplicationUser;
import com.moremusic.moremusicwebapp.services.ApplicationUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/applicationUsers")
public class ApplicationUserController {
    private static final Logger logger = LoggerFactory.getLogger(ApplicationUserController.class);

    private final ApplicationUserService applicationUserService;

    public ApplicationUserController(ApplicationUserService applicationUserService) {
        this.applicationUserService = applicationUserService;
    }

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
}

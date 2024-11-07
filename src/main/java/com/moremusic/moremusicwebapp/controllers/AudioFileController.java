package com.moremusic.moremusicwebapp.controllers;

import com.moremusic.moremusicwebapp.datalayer.entities.ApplicationUser;
import com.moremusic.moremusicwebapp.datalayer.entities.AudioFiles;
import com.moremusic.moremusicwebapp.services.ApplicationUserService;
import com.moremusic.moremusicwebapp.services.AudioFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/audioFiles")
public class AudioFileController {
    private static final Logger logger = LoggerFactory.getLogger(AudioFileController.class);

    private final AudioFileService audioFileService;
    private final ApplicationUserService applicationUserService;

    @Autowired
    public AudioFileController(AudioFileService audioFileService, ApplicationUserService applicationUserService) {
        this.audioFileService = audioFileService;
        this.applicationUserService = applicationUserService;
    }

    @GetMapping
    public List<AudioFiles> getAudioFiles(){
        logger.info("Retrieving audio files.");
        try{
            return audioFileService.getAudioFilesForCurrentUser();
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/shuffle")
    public List<AudioFiles> getAudioFilesShuffle(){
        logger.info("Retrieving shuffled audio files.");
        try{
            return audioFileService.getShuffledAudioFilesForCurrentUser();
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/userPlaylists")
    public List<ApplicationUser> getUserPlaylists(){
        logger.info("Retrieving list of users playlists.");
        try{
            return applicationUserService.getAllUsers();
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

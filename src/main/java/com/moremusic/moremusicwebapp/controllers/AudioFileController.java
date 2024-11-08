package com.moremusic.moremusicwebapp.controllers;

import com.moremusic.moremusicwebapp.datalayer.entities.AudioFiles;
import com.moremusic.moremusicwebapp.services.AudioFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/audioFiles")
public class AudioFileController {
    private static final Logger logger = LoggerFactory.getLogger(AudioFileController.class);

    private final AudioFileService audioFileService;

    @Autowired
    public AudioFileController(AudioFileService audioFileService) {
        this.audioFileService = audioFileService;
    }

    @GetMapping("/files")
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

    @GetMapping("/shuffle/{userId}/{shuffleOn}")
    public List<AudioFiles> getAudioFilesShuffle(@PathVariable long userId, @PathVariable boolean shuffleOn){
        logger.info("Retrieving shuffled audio files.");
        try{
            return audioFileService.getShuffledAudioFilesForUser(userId, shuffleOn);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @GetMapping("/audioFilesForUserPlaylist/{userId}")
    public List<AudioFiles> getAudioFilesForUserPlaylist(@PathVariable Long userId){
        logger.info("Retrieving user playlist audio files.");
        try{
            return audioFileService.getAudioFilesForPlaylistUser(userId);
        }
        catch(Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}

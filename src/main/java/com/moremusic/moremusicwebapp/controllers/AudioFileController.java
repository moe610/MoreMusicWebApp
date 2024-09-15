package com.moremusic.moremusicwebapp.controllers;

import com.moremusic.moremusicwebapp.datalayer.AudioFiles;
import com.moremusic.moremusicwebapp.services.AudioFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/audioFiles")
public class AudioFileController {
    private final AudioFileService audioFileService;

    @Autowired
    public AudioFileController(AudioFileService audioFileService) {
        this.audioFileService = audioFileService;
    }

    @GetMapping
    public List<AudioFiles> getAudioFiles(){
        return audioFileService.getAudioFiles();
    }
}

package com.moremusic.moremusicwebapp.controllers;

import com.moremusic.moremusicwebapp.datalayer.AudioFileModel;
import com.moremusic.moremusicwebapp.services.AudioFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/uploadAudioFiles")
public class UploadAudioFileController {
    private final AudioFileService audioFileService;

    @Autowired
    public UploadAudioFileController(AudioFileService audioFileService) {
        this.audioFileService = audioFileService;
    }
    public static class YoutubeUrlRequest {
        public String youtubeUrl;

        // Add getters and setters
        public String getYoutubeUrl() {
            return youtubeUrl;
        }
    }

    @PostMapping
    public void UploadAudioFile(@RequestBody YoutubeUrlRequest request) {
        try {
            AudioFileModel audioFileModel = audioFileService.DownloadYoutubeVideo(request.getYoutubeUrl());
            if (audioFileModel == null)
                throw new RuntimeException("Audio file already exists");

            if (!audioFileService.SaveAudioFileToDatabase(audioFileModel))
                throw new RuntimeException("Audio file could not be saved");
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

package com.moremusic.moremusicwebapp.controllers;

import com.moremusic.moremusicwebapp.datalayer.models.AudioFileModel;
import com.moremusic.moremusicwebapp.services.AudioFileService;
import com.moremusic.moremusicwebapp.services.UploadMusicService;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/uploadAudioFiles")
public class UploadAudioFileController {
    private static final Logger logger = LoggerFactory.getLogger(UploadAudioFileController.class);

    private final AudioFileService audioFileService;
    private final UploadMusicService uploadMusicService;

    @Autowired
    public UploadAudioFileController(AudioFileService audioFileService, UploadMusicService uploadMusicService) {
        this.audioFileService = audioFileService;
        this.uploadMusicService = uploadMusicService;
    }
    @Getter
    public static class YoutubeUrlRequest {
        public String youtubeUrl;
    }

    @PostMapping
    public void UploadAudioFile(@RequestBody YoutubeUrlRequest request) throws Exception {
        try {
            AudioFileModel audioFileModel = uploadMusicService.DownloadYoutubeVideo(request.getYoutubeUrl());
            if (audioFileModel == null)
                throw new Exception("Audio file already exists");

            if (!audioFileService.SaveAudioFileToDatabase(audioFileModel))
                throw new Exception("Audio file could not be saved");
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e.getMessage());
        }
    }
}

package com.moremusic.moremusicwebapp.services;

import com.moremusic.moremusicwebapp.datalayer.entities.ApplicationUser;
import com.moremusic.moremusicwebapp.datalayer.entities.ApplicationUserPlaylist;
import com.moremusic.moremusicwebapp.datalayer.models.AudioFileModel;
import com.moremusic.moremusicwebapp.datalayer.entities.AudioFiles;
import com.moremusic.moremusicwebapp.datalayer.repository.ApplicationUserPlaylistRepository;
import com.moremusic.moremusicwebapp.datalayer.repository.AudioFileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Collections;

@Service
public class AudioFileService {
    private static final Logger logger = LoggerFactory.getLogger(AudioFileService.class);

    private final AudioFileRepository audioFileRepository;
    private final ApplicationUserService applicationUserService;
    private final ApplicationUserPlaylistRepository applicationUserPlaylistRepository;

    @Autowired
    public AudioFileService(AudioFileRepository audioFileRepository, ApplicationUserService applicationUserService, ApplicationUserPlaylistRepository applicationUserPlaylistRepository) {
        this.audioFileRepository = audioFileRepository;
        this.applicationUserService = applicationUserService;
        this.applicationUserPlaylistRepository = applicationUserPlaylistRepository;
    }

    public List<AudioFiles> getAudioFilesForCurrentUser() {
        ApplicationUser currentUser = applicationUserService.getCurrentUser();
        return audioFileRepository.findByUserPlaylist(currentUser.getId());
    }

    public List<AudioFiles> getAudioFilesForPlaylistUser(long userId){
        return audioFileRepository.findByUserPlaylist(userId);
    }

    public List<AudioFiles> getShuffledAudioFilesForUser(long userId, boolean shuffleOn) {
        List<AudioFiles> audioFiles = audioFileRepository.findByUserPlaylist(userId);

        if (shuffleOn) {
            Collections.shuffle(audioFiles);
        }

        return audioFiles;
    }

    public boolean SaveAudioFileToDatabase(AudioFileModel audioFileModel) throws Exception {
        boolean success = false;

        try{
            AudioFiles audioFiles = new AudioFiles();
            audioFiles.setFileName(audioFileModel.getFileName());
            audioFiles.setTitle(audioFileModel.getTitle());
            audioFiles.setFilePath(audioFileModel.getFilePath());
            audioFiles.setFileType(audioFileModel.getFileType());
            audioFiles.setDuration(audioFileModel.getDuration());

            audioFileRepository.save(audioFiles);

            SaveAudioFileToUserPlaylist(audioFiles.getId());

            success = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception(e);
        }
        return success;
    }

    public void SaveAudioFileToUserPlaylist(long audioFileId) throws Exception {
        try{
            ApplicationUserPlaylist applicationUserPlaylistserplaylist = new ApplicationUserPlaylist();
            applicationUserPlaylistserplaylist.setAudioFileId(audioFileId);
            applicationUserPlaylistserplaylist.setApplicationUserId(applicationUserService.getCurrentUser().getId());

            applicationUserPlaylistRepository.save(applicationUserPlaylistserplaylist);
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new Exception(e);
        }
    }
}

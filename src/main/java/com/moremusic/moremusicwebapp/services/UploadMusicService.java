package com.moremusic.moremusicwebapp.services;

import com.moremusic.moremusicwebapp.datalayer.models.AudioFileModel;
import com.moremusic.moremusicwebapp.datalayer.repository.AudioFileRepository;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UploadMusicService {
    private static final Logger logger = LoggerFactory.getLogger(UploadMusicService.class);

    private final AudioFileRepository audioFileRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;
    private static final String YTDLP_PATH = "yt-dlp";
    private static final String FFMPEG_PATH = "ffmpeg";
    private static final String FILE_TYPE = ".mp3";
    private String fileName;
    private Integer duration;

    public UploadMusicService(AudioFileRepository audioFileRepository) {
        this.audioFileRepository = audioFileRepository;
    }

    public AudioFileModel DownloadYoutubeVideo(String youtubeUrl) throws Exception {
        logger.info("Downloading youtube video: {}", youtubeUrl);
        logger.info("Upload directory: {}", uploadDir);

        youtubeUrl = GetVideoUrl(youtubeUrl);
        AudioFileModel audioFileModel = new AudioFileModel();
        String outputFilePath = uploadDir + File.separator + "%(title)s.m4a";

        List<String> fileNameCommand = List.of(YTDLP_PATH, "--print", "filename", "-o", "%(title)s", "--restrict-filenames", youtubeUrl);
        List<String> ytdlpCommand = List.of(YTDLP_PATH, "--extract-audio", "--audio-format", "m4a", "--restrict-filenames", "-o", outputFilePath, youtubeUrl);

        try{
            createProcess(fileNameCommand);
            if(audioFileRepository.existsByFileName(fileName)){
                logger.error("File already exists.");
                throw new Exception("File already exists");
            }

            createProcess(ytdlpCommand);
            ConvertAudioFileToAac(fileName);
            logger.info("New filename: {}", fileName);

            audioFileModel.setFileName(fileName + FILE_TYPE);
            audioFileModel.setTitle(fileName);
            audioFileModel.setFilePath(uploadDir + File.separator + fileName + FILE_TYPE);
            audioFileModel.setFileType("mp3");
            audioFileModel.setDuration(duration);
        }
        catch (IOException e){
            throw new IOException(e.getMessage());
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }

        return audioFileModel;
    }

    private void ConvertAudioFileToAac(String fileName) throws Exception {
        File inputFile = new File(uploadDir + File.separator + fileName + ".m4a");
        File outputFile = new File(uploadDir + File.separator + fileName + FILE_TYPE);

        if (!outputFile.exists()){
            List<String> ffmpegCommand = List.of(FFMPEG_PATH, "-hide_banner", "-i", inputFile.getAbsolutePath(), "-c:a", "mp3", outputFile.getAbsolutePath());
            createProcess(ffmpegCommand);
        } else{
            logger.error("Audio file already exists.");
            throw new Exception("Audio file already exists.");
        }

        if (inputFile.exists()){
            inputFile.delete();
            logger.info("File {} deleted", fileName);
        }
    }

    private void createProcess(List<String> command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        Process process = processBuilder.start();

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            logger.error("Process builder failed, process exited with code: {}", exitCode);
            throw new Exception("Process builder failed, process exited with code: " + exitCode);
        }

        if (command.get(1).equals("--print")){
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            fileName = output.toString().trim();
        }

        if(command.get(1).equals("-hide_banner")){
            processBuilder.redirectErrorStream(true);
            StringBuilder output = new StringBuilder();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            if (output.isEmpty()) {
                logger.error("FFmpeg output is empty.");
            }

            duration = extractDuration(output.toString());
            if (duration < 0) {
                logger.error("Duration not found in output");
            }
        }
    }

    private Integer extractDuration(String output) {
        // Regular expression to match the duration line
        Pattern pattern = Pattern.compile("time=(\\d{2}:\\d{2}:\\d{2}\\.\\d{2})");
        Matcher matcher = pattern.matcher(output);
        String lastMatchedDuration = null;
        Integer totalSeconds = null;

        while (matcher.find()) {
            lastMatchedDuration = matcher.group(1);
        }

        if (lastMatchedDuration == null) {
            return totalSeconds;
        }

        String[] hms = lastMatchedDuration.split(":");

        String[] secMillis = hms[2].split("\\.");

        int hours = Integer.parseInt(hms[0]);
        int minutes = Integer.parseInt(hms[1]);
        int seconds = Integer.parseInt(secMillis[0]);

        totalSeconds = (hours * 3600) + (minutes * 60) + seconds;

        return totalSeconds;
    }

    private @NotNull String GetVideoUrl(@NotNull String youtubeUrl){
        String videoIdParam = "v=";

        int videoIdIndex = youtubeUrl.indexOf(videoIdParam);
        if(videoIdIndex == -1) return youtubeUrl;

        int nextParamIndex = youtubeUrl.indexOf("&", videoIdIndex);
        if(nextParamIndex == -1) return youtubeUrl;

        return youtubeUrl.substring(0,nextParamIndex);
    }
}

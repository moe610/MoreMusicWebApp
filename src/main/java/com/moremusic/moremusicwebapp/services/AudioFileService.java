package com.moremusic.moremusicwebapp.services;

import com.moremusic.moremusicwebapp.datalayer.AudioFileModel;
import com.moremusic.moremusicwebapp.datalayer.AudioFiles;
import com.moremusic.moremusicwebapp.datalayer.repository.AudioFileRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

@Service
public class AudioFileService {
    private final AudioFileRepository audioFileRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;
    private static final String YTDLP_PATH = "yt-dlp";
    private static final String FFMPEG_PATH = "ffmpeg";
    private String fileName;

    @Autowired
    public AudioFileService(AudioFileRepository audioFileRepository) {
        this.audioFileRepository = audioFileRepository;
    }

    public List<AudioFiles> getAudioFiles() {
        return audioFileRepository.findAll();
    }

    public AudioFileModel DownloadYoutubeVideo(String youtubeUrl) throws Exception {
        youtubeUrl = GetVideoUrl(youtubeUrl);
        AudioFileModel audioFileModel = new AudioFileModel();
        String outputFilePath = uploadDir + File.separator + "%(title)s.m4a";

        // Construct commands
        List<String> fileNameCommand = List.of(YTDLP_PATH, "--print", "filename", "-o", "%(title)s", "--restrict-filenames", youtubeUrl);
        List<String> ytdlpCommand = List.of(YTDLP_PATH, "--extract-audio", "--audio-format", "m4a", "--restrict-filenames", "-o", outputFilePath, youtubeUrl);

        try{
            createProcess(fileNameCommand);
            if(audioFileRepository.existsByFileName(fileName))
                throw new Exception("File already exists");

            createProcess(ytdlpCommand);
            ConvertAudioFileToAac(fileName);

            audioFileModel.setFileName(fileName + ".aac");
            audioFileModel.setTitle(fileName);
            audioFileModel.setFilePath(uploadDir + File.separator + fileName + ".aac");
            audioFileModel.setFileType("aac");
        }
        catch (IOException e){
            throw new IOException(e.getMessage());
        }
        catch (Exception e){
            throw new Exception(e.getMessage());
        }

        return audioFileModel;
    }

    private void ConvertAudioFileToAac(String fileName) throws IOException, InterruptedException {
        File inputFile = new File(uploadDir + File.separator + fileName + ".m4a");
        File outputFile = new File(uploadDir + File.separator + fileName + ".aac");

        if (!outputFile.exists()){
            List<String> ffmpegCommand = List.of(FFMPEG_PATH, "-i", inputFile.getAbsolutePath(), "-c:a", "aac", outputFile.getAbsolutePath());
            createProcess(ffmpegCommand);
        }

        if (inputFile.exists())
            inputFile.delete();
    }

    private void createProcess(List<String> command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        if (!command.get(1).equals("--print"))
            processBuilder.inheritIO();

        Process process = processBuilder.start();

        if (command.get(1).equals("--print")){
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            fileName = output.toString().trim();
        }

        process.waitFor();
    }

    private @NotNull String GetVideoUrl(@NotNull String youtubeUrl){
        String videoIdParam = "v=";

        int videoIdIndex = youtubeUrl.indexOf(videoIdParam);
        if(videoIdIndex == -1) return youtubeUrl;

        int nextParamIndex = youtubeUrl.indexOf("&", videoIdIndex);
        if(nextParamIndex == -1) return youtubeUrl;

        return youtubeUrl.substring(0,nextParamIndex);
    }

    public boolean SaveAudioFileToDatabase(AudioFileModel audioFileModel){
        boolean success = false;

        try{
            AudioFiles audioFiles = new AudioFiles();
            audioFiles.setFileName(audioFileModel.getFileName());
            audioFiles.setTitle(audioFileModel.getTitle());
            audioFiles.setFilePath(audioFileModel.getFilePath());
            audioFiles.setFileType(audioFileModel.getFileType());

            audioFileRepository.save(audioFiles);

            success = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return success;
    }
}

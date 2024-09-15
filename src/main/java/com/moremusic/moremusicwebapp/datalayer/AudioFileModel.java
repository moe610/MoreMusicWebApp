package com.moremusic.moremusicwebapp.datalayer;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AudioFileModel {
    @JsonProperty("title")
    private String fileName;
    private String filePath;
    private String fileType;
    private String title;
    private String artist;
    private String album;
    private Integer duration;

    public AudioFileModel() {}

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getDuration() {
        return duration != null ? duration : 0;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}



package com.moremusic.moremusicwebapp.datalayer.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "audio_files")
public class AudioFiles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "file_name", nullable = false, unique = true)
    private String fileName;
    @Column(name = "file_path", nullable = false, unique = true)
    private String filePath;
    @Column(name = "file_type", nullable = false, unique = true)
    private String fileType;
    @Column(name = "title", nullable = false, unique = true)
    private String title;
    @Column(name = "artist", nullable = true, unique = true)
    private String artist;
    @Column(name = "album", nullable = true, unique = true)
    private String album;
    @Column(name = "duration", nullable = true, unique = true)
    private Integer duration;

    public AudioFiles() {
    }

    public AudioFiles(long id, String fileName, String filePath, String fileType, String title, String artist, String album, Integer duration) {
        this.id = id;
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public AudioFiles(String fileName, String filePath, String fileType, String title, String artist, String album, Integer duration) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.fileType = fileType;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public String getFileType() { return fileType; }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getTitle() { return title; }

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

    @Override
    public String toString() {
        return "AudioFiles{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileType='" + fileType + '\'' +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", duration=" + duration +
                '}';
    }
}

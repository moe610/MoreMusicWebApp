package com.moremusic.moremusicwebapp.datalayer.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
    @Column(name = "file_type", nullable = false)
    private String fileType;
    @Column(name = "title", nullable = false, unique = true)
    private String title;
    @Column(name = "artist", unique = true)
    private String artist;
    @Column(name = "album")
    private String album;
    @Column(name = "duration")
    private Integer duration;

    public AudioFiles() {
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

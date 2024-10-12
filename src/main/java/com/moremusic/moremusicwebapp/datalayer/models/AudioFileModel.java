package com.moremusic.moremusicwebapp.datalayer.models;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}



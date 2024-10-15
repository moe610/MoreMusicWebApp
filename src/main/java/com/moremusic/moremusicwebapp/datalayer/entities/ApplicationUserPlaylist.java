package com.moremusic.moremusicwebapp.datalayer.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_user_playlist")
public class ApplicationUserPlaylist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "application_user_id", nullable = false, unique = false)
    private long applicationUserId;
    @Column(name = "audio_file_id", nullable = false, unique = false)
    private long audioFileId;
}

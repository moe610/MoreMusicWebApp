package com.moremusic.moremusicwebapp.datalayer.repository;

import com.moremusic.moremusicwebapp.datalayer.entities.AudioFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AudioFileRepository extends JpaRepository<AudioFiles, Long> {
    boolean existsByFileName(String fileName);

    @Query("SELECT af FROM AudioFiles af JOIN ApplicationUserPlaylist aup ON af.id = aup.audioFileId WHERE aup.applicationUserId = :userId")
    List<AudioFiles> findByUserPlaylist(@Param("userId") Long userId);
}

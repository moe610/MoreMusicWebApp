package com.moremusic.moremusicwebapp.datalayer.repository;

import com.moremusic.moremusicwebapp.datalayer.entities.AudioFiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudioFileRepository extends JpaRepository<AudioFiles, Long> {
    boolean existsByFileName(String fileName);

    //@Query("SELECT s FROM AudioFiles s WHERE s.fileName = ?1")
    //Optional<AudioFiles> findByFileName(String fileName);
}

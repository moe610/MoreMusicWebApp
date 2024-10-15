package com.moremusic.moremusicwebapp.datalayer.repository;

import com.moremusic.moremusicwebapp.datalayer.entities.ApplicationUserPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationUserPlaylistRepository extends JpaRepository<ApplicationUserPlaylist, Long> {

}

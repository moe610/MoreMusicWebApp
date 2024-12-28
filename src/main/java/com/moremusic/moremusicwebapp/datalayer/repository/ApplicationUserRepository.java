package com.moremusic.moremusicwebapp.datalayer.repository;

import com.moremusic.moremusicwebapp.datalayer.entities.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    Optional<ApplicationUser> findByUsername(String username);
    Optional<ApplicationUser> findByResetToken(String resetToken);

    @Query("SELECT au FROM ApplicationUser au WHERE UPPER(au.email) = :email")
    Optional<ApplicationUser> getApplicationUserByEmail(@Param("email") String email);

    @Query("SELECT au FROM ApplicationUser au WHERE UPPER(au.username) = :userName")
    Optional<ApplicationUser> getApplicationUserByUsername(@Param("userName") String username);

    @Query("SELECT au from ApplicationUser au WHERE au.applicationUserRole = 'USER'")
    List<ApplicationUser> getApplicationUsers();


}

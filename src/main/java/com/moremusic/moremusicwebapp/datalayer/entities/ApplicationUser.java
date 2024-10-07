package com.moremusic.moremusicwebapp.datalayer.entities;

import com.moremusic.moremusicwebapp.datalayer.enums.ApplicationUserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "application_users")
public class ApplicationUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name", nullable = false, unique = false)
    private String firstName;
    @Column(name = "last_name", nullable = false, unique = false)
    private String lastName;
    @Column(name = "user_name", nullable = false, unique = false)
    private String username;
    @Column(name = "email", nullable = false, unique = false)
    private String email;
    @Column(name = "password", nullable = false, unique = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false, unique = false)
    private ApplicationUserRole applicationUserRole;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(applicationUserRole.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonLocked(){
        return true;
    }

    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }
}

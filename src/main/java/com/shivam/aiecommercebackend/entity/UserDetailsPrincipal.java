package com.shivam.aiecommercebackend.entity;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.swing.event.ListDataListener;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public class UserDetailsPrincipal implements UserDetails {

    private final UserEn userEn;

    public UserDetailsPrincipal(UserEn userEn) {
        this.userEn = userEn;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(userEn.getRole().name()));
    }

    @Override
    public String getPassword() {
        return userEn.getPassword();
    }

    @Override
    public String getUsername() {
        return userEn.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userEn.isEnabled();
    }

    public Long getId() {
        return userEn.getId();
    }

    public String getName() {
        return userEn.getName();
    }

    public String getEmail() {
        return userEn.getEmail();
    }

    public LocalDateTime getCreatedAt() {
        return userEn.getCreatedAt();
    }

    public LocalDateTime getUpdatedAt() {
        return userEn.getUpdatedAt();
    }

}

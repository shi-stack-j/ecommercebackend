package com.shivam.aiecommercebackend.service;

import com.shivam.aiecommercebackend.entity.UserDetailsPrincipal;
import com.shivam.aiecommercebackend.entity.UserEn;
import com.shivam.aiecommercebackend.repository.UserEnRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserEnRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEn userEn=userRepo.findByUsernameIgnoreCase(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found with the given username :- "+username));

        return new UserDetailsPrincipal(userEn);
    }
}

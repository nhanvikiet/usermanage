package com.example.authservice.service;

import com.example.authservice.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;



@Service
public class CustomUserDetailsService implements UserDetailsService{
    private final UserRepository repo;

    public CustomUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repo.findByUsername(username)
                .map(user -> User
                        .withUsername(user.getUsername())
                        .password(user.getPassword())
                        .roles(user.getRole().replace("ROLE_", ""))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }




}

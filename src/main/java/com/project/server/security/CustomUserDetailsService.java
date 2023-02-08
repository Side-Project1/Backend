package com.project.server.security;


import com.project.server.entity.Users;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + email)
        );

        return CustomUserPrincipal.create(users);
    }

    @Transactional
    public UserDetails loadUserById(UUID userId) {
        Users users = userRepository.findById(userId).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", userId)
        );

        return CustomUserPrincipal.create(users);
    }
}
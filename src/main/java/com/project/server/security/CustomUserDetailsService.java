package com.project.server.security;


import com.project.server.entity.User;
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
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with userId : " + userId)
        );

        return CustomUserPrincipal.create(user);
    }

    @Transactional
    public UserDetails loadUserById(UUID uuid) {
        User user = userRepository.findById(uuid).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", uuid)
        );

        return CustomUserPrincipal.create(user);
    }
}
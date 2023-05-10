package com.project.server.security;


import com.project.server.entity.Users;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.repository.UsersRepository;
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

    private final UsersRepository usersRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userId)
            throws UsernameNotFoundException {
        Users users = usersRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with userId : " + userId)
        );

        return CustomUserPrincipal.create(users);
    }

    @Transactional
    public UserDetails loadUserById(UUID uuid) {
        Users users = usersRepository.findById(uuid).orElseThrow(
            () -> new ResourceNotFoundException("User", "id", uuid)
        );

        return CustomUserPrincipal.create(users);
    }
}
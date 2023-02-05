package com.project.server.controller;


import com.project.server.entity.User;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.http.ApiResponse;
import com.project.server.http.LoginRequest;
import com.project.server.repository.UserRepository;
import com.project.server.security.CustomUserPrincipal;
import com.project.server.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/user/me")
    public User getCurrentUser(@RequestBody LoginRequest loginRequest) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());   //테스트 용
        return userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", loginRequest.getEmail()));
    }



}

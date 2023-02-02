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
        return userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", loginRequest.getEmail()));
    }


    @GetMapping("/auth/token")
    public ApiResponse kakaoCallback(@RequestParam String token){
        String response = "성공적으로 카카오 로그인 API 코드를 불러왔습니다.";
        System.out.println(token);
        return new ApiResponse(true, response);
    }
}

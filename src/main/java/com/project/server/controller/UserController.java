package com.project.server.controller;


import com.project.server.entity.User;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.http.request.LoginRequest;
import com.project.server.repository.UserRepository;
import com.project.server.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name="User", description = "유저 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final EmailService emailService;
    private final UserRepository userRepository;

    @Operation(summary = "사용자 테스트", description = "사용자 테스트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/me")
    public User getCurrentUser(@RequestBody LoginRequest loginRequest) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());   //테스트 용
        return userRepository.findByUserId(loginRequest.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", loginRequest.getUserId()));
    }

}

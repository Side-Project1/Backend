package com.project.server.controller;

import com.project.server.entity.User;
import com.project.server.repository.UserRepository;
import com.project.server.service.EmailService;
import com.project.server.util.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/me")
    public void getCurrentUser(@ApiIgnore @AuthUser User user) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getClass());   //테스트 용
        System.out.println(SecurityContextHolder.getContext().getAuthentication());   //테스트 용
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());   //테스트 용
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass());   //테스트 용
        System.out.println(user.getEmail());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());   //테스트 용
    }

}

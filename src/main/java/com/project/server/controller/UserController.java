package com.project.server.controller;


import com.project.server.entity.User;
import com.project.server.service.UserService;
import com.project.server.util.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.transaction.Transactional;

@Tag(name="User", description = "유저 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @Transactional
    @Operation(summary = "사용자 테스트", description = "사용자 테스트")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/me")
    public ResponseEntity getCurrentUser(@ApiIgnore @AuthUser User user) {
        return new ResponseEntity(userService.getUserInfo(user), HttpStatus.OK);
    }

}

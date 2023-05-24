package com.project.server.controller;


import com.project.server.entity.Users;
import com.project.server.http.request.AccountRequest;
import com.project.server.http.request.PasswordRequest;
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
    @Operation(summary = "마이페이지", description = "마이페이지")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/me")
    public ResponseEntity getCurrentUser(@ApiIgnore @AuthUser Users users) {
        return new ResponseEntity(userService.getUserInfo(users), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/account")
    public ResponseEntity changeAccount(@ApiIgnore @AuthUser Users users, @RequestBody AccountRequest accountRequest) {
        return userService.changeAcount(users, accountRequest);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/account")
    public ResponseEntity changePhone(@ApiIgnore @AuthUser Users users, @RequestParam(name = "phone") String phone) {
        return userService.changePhone(users, phone);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/account")
    public ResponseEntity changePassword(@ApiIgnore @AuthUser Users users, @RequestBody PasswordRequest passwordRequest) {
        return userService.changePassword(users, passwordRequest);
    }
}

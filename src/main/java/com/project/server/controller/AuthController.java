package com.project.server.controller;

import com.project.server.http.response.ApiResponse;
import com.project.server.http.request.LoginRequest;
import com.project.server.http.request.SignUpRequest;
import com.project.server.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Value("${my.test}")
    private String test;

    @Operation(summary = "사용자 테스트", description = "사용자 테스트")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        System.out.println(test);
        return authService.login(loginRequest, response);

    }

    @Operation(summary = "사용자 테스트", description = "사용자 테스트")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("/signup")
    public ResponseEntity registerUser(@RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }

    @Operation(summary = "사용자 테스트", description = "사용자 테스트")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/refreshtoken/{userId}")
    public ResponseEntity getRefreshToken(@PathVariable(name = "userId") UUID id, HttpServletResponse rep){
        return authService.renewalRefreshToken(id, rep);
    }






    @ApiIgnore
    @GetMapping("/token")
    public ResponseEntity kakaoCallback(){
        return new ResponseEntity(new ApiResponse("성공적으로 카카오 로그인 API 코드를 불러왔습니다.", HttpStatus.OK), HttpStatus.OK);
    }
//
//    @GetMapping("/error")
//    public ApiResponse errorPage(@RequestParam String error) {
//        return new ApiResponse(true, error);
//    }
}

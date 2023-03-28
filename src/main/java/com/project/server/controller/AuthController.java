package com.project.server.controller;

import com.project.server.http.response.ApiRes;
import com.project.server.http.request.LoginRequest;
import com.project.server.http.request.SignUpRequest;
import com.project.server.repository.UserRepository;
import com.project.server.security.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Tag(name="Auth", description = "Auth API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Operation(tags = "Auth", summary = "사용자 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return authService.login(loginRequest, response);
    }

    @Operation(tags = "Auth", summary = "회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @PostMapping("/signup")
    public ResponseEntity registerUser(@RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }

    @Operation(tags = "Auth", summary = "토큰 재발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK !!"),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST !!"),
            @ApiResponse(responseCode = "404", description = "NOT FOUND !!"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR !!")
    })
    @GetMapping("/refreshtoken/{userId}")
    public ResponseEntity getRefreshToken(@PathVariable(name = "userId") UUID id, HttpServletResponse rep){
        return authService.renewalRefreshToken(id, rep);
    }






    @ApiIgnore
    @GetMapping("/token")
    public ResponseEntity kakaoCallback(@RequestParam String Authorization){
        return new ResponseEntity(new ApiRes("성공적으로 카카오 로그인 API 코드를 불러왔습니다.", HttpStatus.OK), HttpStatus.OK);
    }
//
//    @GetMapping("/error")
//    public ApiResponse errorPage(@RequestParam String error) {
//        return new ApiResponse(true, error);
//    }
}

package com.project.server.controller;

import com.project.server.http.response.ApiResponse;
import com.project.server.http.request.LoginRequest;
import com.project.server.http.request.SignUpRequest;
import com.project.server.security.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity authenticateUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        return authService.login(loginRequest, response);
    }

    @PostMapping("/signup")
    public ResponseEntity registerUser(@RequestBody SignUpRequest signUpRequest) {
        return authService.signUp(signUpRequest);
    }

    @GetMapping("/token")
    public ResponseEntity kakaoCallback(){
        return new ResponseEntity(new ApiResponse("성공적으로 카카오 로그인 API 코드를 불러왔습니다.", HttpStatus.OK), HttpStatus.OK);
    }

    @GetMapping("/refreshtoken/{userId}")
    public ResponseEntity getRefreshToken(@PathVariable(name = "userId") UUID id, HttpServletResponse rep){
        return authService.renewalRefreshToken(id, rep);
    }
//
//    @GetMapping("/error")
//    public ApiResponse errorPage(@RequestParam String error) {
//        return new ApiResponse(true, error);
//    }
}

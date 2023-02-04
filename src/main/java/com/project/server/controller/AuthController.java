package com.project.server.controller;

import com.project.server.dto.DefaultResponse;
import com.project.server.entity.Role;
import com.project.server.entity.Token;
import com.project.server.entity.User;
import com.project.server.exception.BadRequestException;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.http.ApiResponse;
import com.project.server.http.AuthResponse;
import com.project.server.http.LoginRequest;
import com.project.server.http.SignUpRequest;
import com.project.server.repository.TokenRepository;
import com.project.server.repository.UserRepository;
import com.project.server.security.AuthProvider;
import com.project.server.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@RestController

@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String, String> tokens = tokenProvider.createToken(authentication);
        Optional<User> userOp=userRepository.findByEmail(loginRequest.getEmail());
        tokenRepository.findById(userOp.get().getId());
        tokenRepository.save(
                Token.builder()
                        .user(userOp.get())
                        .refreshToken(tokens.get("refreshToken")).build());

        return ResponseEntity.ok(new AuthResponse(tokens.get("accessToken"), tokens.get("refreshToken")));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new BadRequestException("Email address already in use.");
        }

        // Creating user's account
        User user = User.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .provider(AuthProvider.local)
                .role(Role.USER)
                .build();

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/user/me")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "User registered successfully@"));
    }

    @GetMapping("/refreshtoken/{userId}")
    public DefaultResponse getRefreshToken(@PathVariable(name = "userId") Long id, HttpServletRequest req, HttpServletResponse rep){
        return tokenProvider.getTokenFromRefreshToken(id, rep);
    }
}

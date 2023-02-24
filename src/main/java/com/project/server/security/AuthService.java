package com.project.server.security;

import com.project.server.entity.Role;
import com.project.server.entity.User;
import com.project.server.http.request.LoginRequest;
import com.project.server.http.request.SignUpRequest;
import com.project.server.http.response.ApiResponse;
import com.project.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public ResponseEntity signUp(SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiResponse("해당 이메일을 사용하고 있습니다.", HttpStatus.CONFLICT), HttpStatus.CONFLICT);
        }

        try {
            User user = User.builder()
                    .name(signUpRequest.getName())
                    .email(signUpRequest.getEmail())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .provider(AuthProvider.local)
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            return new ResponseEntity(new ApiResponse("회원가입 성공", HttpStatus.CREATED), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(e.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity login(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );
            Map<String, String> tokens = tokenProvider.createToken(authentication);
            response.setHeader("Authorization", tokens.get("accessToken"));
            return new ResponseEntity(new ApiResponse("로그인 성공", HttpStatus.OK), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(new ApiResponse(e.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity renewalRefreshToken(UUID id, HttpServletResponse rep) {
        return tokenProvider.renewalRefreshToken(id, rep);
    }
}

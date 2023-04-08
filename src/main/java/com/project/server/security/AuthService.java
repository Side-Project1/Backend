package com.project.server.security;

import com.project.server.entity.Role;
import com.project.server.entity.User;
import com.project.server.http.request.LoginRequest;
import com.project.server.http.request.SignUpRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Slf4j
@Service
public class AuthService {
    private final String pattern = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$"; // 영문, 숫자, 특수문자 , 8~20자리
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    public ResponseEntity signUp(SignUpRequest signUpRequest) {
        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity(new ApiRes("해당 이메일을 사용하고 있습니다.", HttpStatus.CONFLICT), HttpStatus.CONFLICT);
        }
        if(!Pattern.compile(pattern).matcher(signUpRequest.getPassword()).find()) {
            return new ResponseEntity(new ApiRes("비밀번호는 영어, 숫자, 특수문자를 사용하고 8~20자리이어야 합니다.", HttpStatus.CONFLICT), HttpStatus.CONFLICT);
        }

        try {
            User user = User.builder()
                    .userId(signUpRequest.getUserId())
                    .password(passwordEncoder.encode(signUpRequest.getPassword()))
                    .userName(signUpRequest.getUserName())
                    .phone(signUpRequest.getPhone())
                    .email(signUpRequest.getEmail())
                    .birthday(signUpRequest.getBirthday())
                    .provider(AuthProvider.local)
                    .role(Role.USER)
                    .gender(signUpRequest.getGender())
                    .job(signUpRequest.getJob())
//                    .category(signUpRequest.getCategory())
                    .path(signUpRequest.getPath())
                    .build();
            userRepository.save(user);
            log.info("회원 가입 성공");
            return new ResponseEntity(new ApiRes("회원가입 성공", HttpStatus.CREATED), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes(e.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity login(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Optional<User> user = userRepository.findByUserId(loginRequest.getUserId());
            if (user.isPresent()) {
                if(passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())){
                    String tokens = tokenProvider.createToken(user.get().getId());
                    response.setHeader("Authorization", tokens);
                    log.info("로그인 성공, 토근 발급 완료");
                    return new ResponseEntity(new ApiRes("로그인 성공", HttpStatus.OK), HttpStatus.OK);
                }else {
                    throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
                }
            }else {
                throw new IllegalArgumentException("아이디를 찾을 수 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(new ApiRes(e.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity(new ApiRes(e.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity renewalRefreshToken(UUID id, HttpServletResponse rep) {
        return tokenProvider.renewalRefreshToken(id, rep);
    }

}

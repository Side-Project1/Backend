package com.project.server.security;

import com.project.server.config.AppProperties;
import com.project.server.http.request.LoginRequest;
import com.project.server.http.response.ApiRes;
import com.project.server.entity.User;
import com.project.server.entity.UserToken;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.repository.UserRepository;
import com.project.server.repository.UserTokenRepository;
import com.project.server.util.CustomCookie;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {

    private final AppProperties appProperties;
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;

    @Transactional
    public Map<String, String> createToken(Authentication authentication) {
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Map<String, String> tokens = new HashMap<>();

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userPrincipal.getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec()))      // 30분
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec()* 2 * 24 * 7)) // 일주일
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();

        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        UserToken userToken = UserToken.builder()
                .refreshToken(refreshToken)
                .build();
        userTokenRepository.save(userToken);
        user.setUserToken(userToken);
        userRepository.save(user);

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    @Transactional
    public Map<String, String> createTokenForLocal(LoginRequest loginRequest) {

        Date now = new Date();
        Map<String, String> tokens = new HashMap<>();

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(loginRequest.getUserId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec()))      // 30분
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec()* 2 * 24 * 7)) // 일주일
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();

        User user = userRepository.findByUserId(loginRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", loginRequest.getUserId()));
        UserToken userToken = UserToken.builder()
                .refreshToken(refreshToken)
                .build();
        userTokenRepository.save(userToken);
        user.setUserToken(userToken);
        userRepository.save(user);

        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    public String createAccessToken(UUID uuid) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(uuid))
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec()))      // 30분
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
        return UUID.fromString(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    public boolean reGenerateRefreshToken(UUID uuid) {
        log.info("refreshToken 재발급 요청");

        User user = userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User", "id", uuid));
        String refreshToken = user.getUserToken().getRefreshToken();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        // refreshToken 정보가 존재하지 않는 경우
        if(refreshToken == null) {
            log.info("refreshToken 정보가 존재하지 않습니다.");
            return false;
        }

        // refreshToken 만료 여부 체크
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(refreshToken);
            log.info("refreshToken 만료되지 않았습니다.");
            return true;
        } catch(ExpiredJwtException e) {    // refreshToken이 만료된 경우 재발급
            UserToken userToken = UserToken.builder()
                    .refreshToken(Jwts.builder()
                                .setSubject(String.valueOf(user.getId()))
                                .setExpiration(expiryDate) // 시간 변경 예정
                                .setIssuedAt(new Date(System.currentTimeMillis()))
                                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                                .compact())
                    .build();
            user.setUserToken(userToken);

            log.info("refreshToken 재발급 완료 : {}", "Bearer " + user.getUserToken().getRefreshToken());
            return true;
        } catch(Exception e) {
            log.error("refreshToken 재발급 중 문제 발생 : {}", e.getMessage());
            return false;
        }
    }

    public ResponseEntity renewalRefreshToken(UUID uuid, HttpServletResponse rep) {
        User user = userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User", "id", uuid));
        String refreshToken = user.getUserToken().getRefreshToken();
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(refreshToken);
            String accessToken = createAccessToken(uuid);
            CustomCookie.addCookie(rep, "accessToken", accessToken, 60*30);
            return new ResponseEntity(new ApiRes("access 토근 재발급", HttpStatus.OK), HttpStatus.OK);
        } catch(ExpiredJwtException e) {    // refreshToken이 만료된 경우 재발급
            return new ResponseEntity(new ApiRes("refresh 토큰 만료, access 토큰 재발급 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity(new ApiRes("재발급 문제 발생", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}

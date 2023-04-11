package com.project.server.security;

import com.project.server.config.AppProperties;
import com.project.server.http.response.ApiRes;
import com.project.server.entity.User;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProvider {
    private final RedisTemplate<String, String> redisTemplate;
    private final AppProperties appProperties;
    private final UserRepository userRepository;

    public String createTokenOAuth2(Authentication authentication) {
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        return createToken(userPrincipal.getId());
    }

    public String createToken(UUID uuid) {
        Date now = new Date();

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(uuid))
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + appProperties.getTokenExpirationMsec()))      // 30분
                .signWith(SignatureAlgorithm.HS512, appProperties.getTokenSecret())
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + appProperties.getTokenExpirationMsec()* 2 * 24 * 7)) // 일주일
                .signWith(SignatureAlgorithm.HS512, appProperties.getTokenSecret())
                .compact();

        User user = userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User", "id", uuid));
        redisTemplate.opsForValue().set(user.getId().toString(), refreshToken, appProperties.getTokenExpirationMsec()* 2 * 24 * 7, TimeUnit.MILLISECONDS);
        log.info("refresh Token : {} ", refreshToken);
        return accessToken;
    }

    public String createAccessToken(UUID uuid) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(String.valueOf(uuid))
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + appProperties.getTokenExpirationMsec()))      // 30분
                .signWith(SignatureAlgorithm.HS512, appProperties.getTokenSecret())
                .compact();
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
        return UUID.fromString(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getTokenSecret()).parseClaimsJws(authToken);
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
        ValueOperations<String, String> valueOperation = redisTemplate.opsForValue();
        String refreshToken = valueOperation.get(user.getId().toString());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getTokenExpirationMsec()* 2 * 24 * 7);

        // refreshToken 정보가 존재하지 않는 경우
        if(refreshToken == null) {
            log.info("refreshToken 정보가 존재하지 않습니다.");
            return false;
        }

        // refreshToken 만료 여부 체크
        try {
            Jwts.parser().setSigningKey(appProperties.getTokenSecret()).parseClaimsJws(refreshToken);
            log.info("refreshToken 만료되지 않았습니다.");
            return true;
        } catch(ExpiredJwtException e) {    // refreshToken이 만료된 경우 재발급
            String newRefreshToken =Jwts.builder()
                    .setSubject(String.valueOf(user.getId()))
                    .setIssuedAt(new Date())
                    .setExpiration(expiryDate) // 시간 변경 예정
                    .signWith(SignatureAlgorithm.HS512, appProperties.getTokenSecret())
                    .compact();
            redisTemplate.opsForValue().set(user.getId().toString(), newRefreshToken, appProperties.getTokenExpirationMsec()* 2 * 24 * 7, TimeUnit.MILLISECONDS);
            log.info("refreshToken 재발급 완료 : {}", "Bearer " + newRefreshToken);
            return true;
        } catch(Exception e) {
            log.error("refreshToken 재발급 중 문제 발생 : {}", e.getMessage());
            return false;
        }
    }

    public ResponseEntity renewalRefreshToken(UUID uuid, HttpServletResponse response) {
        try {
            User user = userRepository.findById(uuid).orElseThrow(() -> new ResourceNotFoundException("User", "id", uuid));
            ValueOperations<String, String> valueOperation = redisTemplate.opsForValue();
            String refreshToken = valueOperation.get(user.getId().toString());
            log.info("refresh Token : {}", refreshToken);
            Jwts.parser().setSigningKey(appProperties.getTokenSecret()).parseClaimsJws(refreshToken);   // 만료 체크
            String accessToken = createAccessToken(uuid);
            response.setHeader("Authorization", accessToken);
            return new ResponseEntity(new ApiRes("access 토근 재발급", HttpStatus.OK), HttpStatus.OK);
        } catch(ExpiredJwtException e) {    // refreshToken이 만료된 경우 재발급
            return new ResponseEntity(new ApiRes("refresh 토큰 만료, access 토큰 재발급 실패", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch(Exception e) {
            return new ResponseEntity(new ApiRes("재발급 문제 발생", HttpStatus.BAD_REQUEST, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
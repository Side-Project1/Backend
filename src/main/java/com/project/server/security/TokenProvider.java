package com.project.server.security;

import com.project.server.config.AppProperties;
import com.project.server.dto.DefaultResponse;
import com.project.server.entity.Token;
import com.project.server.entity.User;
import com.project.server.exception.ResourceNotFoundException;
import com.project.server.repository.TokenRepository;
import com.project.server.repository.UserRepository;
import com.project.server.util.CustomCookie;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class TokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private AppProperties appProperties;
    private UserRepository userRepository;
    private TokenRepository tokenRepository;

    public TokenProvider(AppProperties appProperties, UserRepository userRepository) {
        this.appProperties = appProperties;
        this.userRepository = userRepository;
    }

    @Transactional
    public Map<String, String> createToken(Authentication authentication) {
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Map<String, String> tokens = new HashMap<>();

        String accessToken = Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))   // email로 바꾸면 관련된 것들 다 email로 바꿔야함
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec()))      // 30분
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();

        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec()* 2 * 24 * 7)) // 일주일
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);

        return tokens;
    }

    public String createAccessToken(Long id) {
        Date now = new Date();

        return Jwts.builder()
                .setSubject(Long.toString(id))
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec()))      // 30분
                .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                .compact();
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(appProperties.getAuth().getTokenSecret())
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public boolean reGenerateRefreshToken(Long userId) {
        logger.info("refreshToken 재발급 요청");
        Token token = tokenRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("Token", "id", userId));
        String refreshToken = token.getRefreshToken();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + appProperties.getAuth().getTokenExpirationMsec());

        // refreshToken 정보가 존재하지 않는 경우
        if(refreshToken == null) {
            logger.info("refreshToken 정보가 존재하지 않습니다.");
            return false;
        }

        // refreshToken 만료 여부 체크
        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(refreshToken);
            logger.info("refreshToken 만료되지 않았습니다.");
            return true;
        } catch(ExpiredJwtException e) {    // refreshToken이 만료된 경우 재발급
            token.setRefreshToken(Jwts.builder()
                    .setSubject(Long.toString(token.getId()))
                    .setExpiration(expiryDate) // 시간 변경 예정
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .signWith(SignatureAlgorithm.HS512, appProperties.getAuth().getTokenSecret())
                    .compact());
            logger.info("refreshToken 재발급 완료 : {}", "Bearer " + token.getRefreshToken());
            return true;
        } catch(Exception e) {
            logger.error("refreshToken 재발급 중 문제 발생 : {}", e.getMessage());
            return false;
        }
    }

    public DefaultResponse getTokenFromRefreshToken(Long userId, HttpServletResponse rep) {
        Token token = tokenRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("Token", "id", userId));
        String refreshToken = token.getRefreshToken();

        try {
            Jwts.parser().setSigningKey(appProperties.getAuth().getTokenSecret()).parseClaimsJws(refreshToken);
            String accessToken = createAccessToken(userId);
            System.out.println(accessToken);
            CustomCookie.addCookie(rep, "accessToken", accessToken, 60*30);
            return new DefaultResponse("access 토근 재발급", HttpStatus.OK);
        } catch(ExpiredJwtException e) {    // refreshToken이 만료된 경우 재발급
            return new DefaultResponse("refresh 토큰 만료, access 토큰 재발급 실패", HttpStatus.BAD_REQUEST, e.getMessage());
        } catch(Exception e) {
            return new DefaultResponse("재발급 문제 발생", HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}

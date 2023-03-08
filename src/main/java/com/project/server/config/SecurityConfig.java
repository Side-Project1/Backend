package com.project.server.config;

import com.project.server.security.handler.CustomAccessDeniedHandler;
import com.project.server.security.handler.CustomAuthenticationEntryPoint;
import com.project.server.security.TokenAuthenticationFilter;
import com.project.server.security.oauth2.CustomAuthorizationRequestRepository;
import com.project.server.security.oauth2.CustomOAuth2AuthenticationFailureHandler;
import com.project.server.security.oauth2.CustomOAuth2AuthenticationSuccessHandler;
import com.project.server.security.oauth2.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAuthorizationRequestRepository customAuthorizationRequestRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2AuthenticationSuccessHandler oAuth2AuthorizationSuccessHandler;
    private final CustomOAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        //Spring Seucrity 로직을 수행하지 않고 아래 요청에 접근
        return (web) -> web.ignoring().
                antMatchers("/images/**", "/js/**", "/webjars/**", "/swagger-ui/**",
                        "/swagger-resources/**", "/v3/api-docs", "/favicon/**");
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 토큰 기반 인증이므로 세션을 사용 안함
                    .and()
                .csrf().disable()   // csrf 보안 토큰 해제
                .formLogin().disable()
                .httpBasic().disable()  // rest api 만을 고려하여 기본 설정 해제, 해제 안할 시 기본 로그인 창 나옴
                .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler)
                    .and()
                .authorizeHttpRequests()    // 요청에 대한 사용권한 체크
//                    .antMatchers( "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs", "/favicon*").permitAll()
//                    .antMatchers("/login/**").permitAll()
//                    .antMatchers("/auth/**").permitAll()
//                    .antMatchers("/oauth2/**").permitAll()
//                    .antMatchers("/api/v1/email/**").permitAll()
                    .anyRequest().permitAll()
                    .and()
                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorize")
                        .authorizationRequestRepository(customAuthorizationRequestRepository)
                        .and()
                    .redirectionEndpoint()
                        .baseUri("/oauth2/callback/**")
                        .and()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService)
                        .and()
                    .successHandler(oAuth2AuthorizationSuccessHandler)
                    .failureHandler(oAuth2AuthenticationFailureHandler);
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();

    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(false);
        configuration.setExposedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, " +
                "Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers", "Content-Disposition"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

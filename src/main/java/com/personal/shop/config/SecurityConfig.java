package com.personal.shop.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.lang.reflect.AccessibleObject;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // FilterChain
    // 모든 유저의 요청과 서버의 응답 사이에 자동으로 실행해주고 싶은 코드를 담는 곳
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                // CSRF 기능 켜기
                .csrf(csrf
                -> csrf
                    .csrfTokenRepository(csrfTokenRepository())
                    .ignoringRequestMatchers("/login", "/register"))

                .authorizeHttpRequests(authorize
                -> authorize
                    .requestMatchers("/login", "/register").permitAll()
                    .requestMatchers("/myPage", "/itemInfo/**", "/noticeInfo/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")
                    .requestMatchers("/**").permitAll())

                .formLogin(formLogin
                -> formLogin
                    .loginPage("/login") // The URL to the login page
                    .defaultSuccessUrl("/list", true)
                    .failureUrl("/login?error=true")
                    .permitAll())

                .logout(logout
                -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/list") // redirect to Item List page
                    .permitAll())

                // 인증되지 않은 사용자의 요청에 의한 설정
                .exceptionHandling(exceptionHandling
                -> exceptionHandling
                    // 401, 403
                    .authenticationEntryPoint(((request, response, authException) -> {
                        response.setStatus(HttpStatus.UNAUTHORIZED.value());
                        response.getWriter().write("로그인이 필요합니다.");
                    }))
                    // 404
                    .accessDeniedHandler(customAccessDeniedHandler()))

                // Enable CORS with specific configurations
                .cors(cors
                -> cors
                    .configurationSource(corsConfigurationSource()))

                .addFilterBefore(characterEncodingFilter(), org.springframework.web.filter.CorsFilter.class);

        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // CSRF 기능 켜기
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository csrfRepository = new HttpSessionCsrfTokenRepository();
        csrfRepository.setHeaderName("X-XSRF-TOKEN");

        return csrfRepository;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOriginPattern("http://localhost:8080");
        config.addAllowedHeader("*");
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }

    @Bean
    public AccessDeniedHandler customAccessDeniedHandler() {

        return ((request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("엑세스 거부 : 권한이 없습니다.");
        });
    }

    @Bean
    public CharacterEncodingFilter characterEncodingFilter() {
        CharacterEncodingFilter charFilter = new CharacterEncodingFilter();
        charFilter.setEncoding("UTF-8");
        charFilter.setForceEncoding(true);

        return charFilter;
    }
}

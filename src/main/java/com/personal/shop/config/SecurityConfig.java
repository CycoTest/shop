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
                // CSRF 기능 끄기
                .csrf(AbstractHttpConfigurer::disable)
//                // CSRF 기능 켜기
//                .csrf(csrf
//                -> csrf
//                    .csrfTokenRepository(csrfTokenRepository())
//                    .ignoringRequestMatchers("/login", "/register"))

                .authorizeHttpRequests(authorize
                -> authorize
                    .requestMatchers("/login", "/register").permitAll()
                    .requestMatchers("/myPage", "/itemInfo/**", "/noticeInfo/**").hasAnyAuthority("USER", "ADMIN")
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

                // 인증되지 않은 사용자의 요청에 의한 401 상태 코드를 반환하도록 설정
                .exceptionHandling(exceptionHandling
                -> exceptionHandling
                    .authenticationEntryPoint(((request, response, authException) -> response.sendRedirect("/login")))
                        .accessDeniedHandler(customAccessDeniedHandler()))

                // Enable CORS with specific configurations
                .cors(cors
                -> cors
                    .configurationSource(corsConfigurationSource()));

        return httpSecurity.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

//    // CSRF 기능 켜기
//    @Bean
//    public CsrfTokenRepository csrfTokenRepository() {
//        HttpSessionCsrfTokenRepository csrfRepository = new HttpSessionCsrfTokenRepository();
//        csrfRepository.setHeaderName("X-XSRF-TOKEN");
//
//        return csrfRepository;
//    }

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

        return ((request, response, accessDeniedException)
                -> response.sendRedirect("/access-denied"));
    }
}

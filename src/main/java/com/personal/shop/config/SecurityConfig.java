package com.personal.shop.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    // FilterChain
    // 모든 유저의 요청과 서버의 응답 사이에 자동으로 실행해주고 싶은 코드를 담는 곳
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // CSRF 기능 끄기
        httpSecurity.csrf( (csrf) -> csrf.disable());

        // 인증되지 않은 사용자의 요청에 의한 401 상태 코드를 반환하도록 설정
        httpSecurity.exceptionHandling((exceptionHandling)
                -> exceptionHandling
                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));


        httpSecurity.authorizeHttpRequests( (authorize)
                -> authorize
                    .requestMatchers("/myPage", "/itemInfo/**").authenticated()
                    .requestMatchers("/**").permitAll()
        );

        httpSecurity.formLogin((formLogin)
                -> formLogin
                    .loginPage("/login") // The URL to the login page
                    .defaultSuccessUrl("/list", true)
                    .failureUrl("/login")
        );

        httpSecurity.logout((logout)
                -> logout
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/list") // redirect to Item List page
                    .permitAll()
        );

        // Enable CORS with specific configurations
        httpSecurity.cors((cors) -> cors.configurationSource(corsConfigurationSource()));

        return httpSecurity.build();
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
}

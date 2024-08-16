package com.personal.shop.config;

import com.personal.shop.config.module.AuthenticationSecurityConfig;
import com.personal.shop.config.module.BasicSecurityConfig;
import com.personal.shop.config.module.ExceptionHandlingSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final BasicSecurityConfig basicSecurityConfig;
    private final AuthenticationSecurityConfig authSecurityConfig;
    private final ExceptionHandlingSecurityConfig exceptionHandlingSecurityConfig;

    // FilterChain
    // 모든 유저의 요청과 서버의 응답 사이에 자동으로 실행해주고 싶은 코드를 담는 곳
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        basicSecurityConfig.configure(http);
        authSecurityConfig.configure(http);
        exceptionHandlingSecurityConfig.configure(http);

        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }
}

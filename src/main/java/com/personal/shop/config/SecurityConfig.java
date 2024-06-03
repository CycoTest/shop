package com.personal.shop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf( (csrf) -> csrf.disable());
        httpSecurity.authorizeHttpRequests( (authorize)
                -> authorize.requestMatchers("/**").permitAll()
        );

        httpSecurity.formLogin((formLogin)
                -> formLogin.loginPage("/member")
                .defaultSuccessUrl("/member/login")
//                .failureUrl("/fail")
        );

        return httpSecurity.build();
    }

    // FilterChain
    // 모든 유저의 요청과 서버의 응답 사이에 자동으로 실행해주고 싶은 코드를 담는 곳
}

package com.personal.shop.config;

import com.personal.shop.component.CustomAuthEntryPoint;
import com.personal.shop.config.module.SecurityPath;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final CustomAuthEntryPoint customAuthEntryPoint;

    // FilterChain
    // 모든 유저의 요청과 서버의 응답 사이에 자동으로 실행해주고 싶은 코드를 담는 곳
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .addFilterBefore(characterEncodingFilter(), CorsFilter.class)
            .csrf(this::configureCsrf) // CSRF 기능 켜기
            .cors(this::configureCors) // Enable CORS with specific configurations
            .authorizeHttpRequests(this::configureAuthorizeHttpRequests) // Authentication
            .formLogin(this::configureFormLogin)
            .logout(this::configureLogout)
            .exceptionHandling(this::configureExceptionHandling);

        return http.build();
    }

    private void configureCsrf(CsrfConfigurer<HttpSecurity> csrf) throws RuntimeException {
        csrf
            .csrfTokenRepository(csrfTokenRepository())
            .ignoringRequestMatchers(SecurityPath.LOGIN.getPath(), SecurityPath.REGISTER.getPath());
    }

    private void configureCors(CorsConfigurer<HttpSecurity> cors) {
        cors
            .configurationSource(corsConfigurationSource());
    }

    private void configureAuthorizeHttpRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry authorize)
            throws RuntimeException {
        authorize
            .requestMatchers(
                    SecurityPath.LOGIN.getPath(),
                    SecurityPath.REGISTER.getPath(),
                    SecurityPath.ACTUATOR.getPath(),
                    SecurityPath.DETAIL.getPath()
            ).permitAll()

            .requestMatchers(
                    SecurityPath.API_CHECK_AUTH.getPath()
            ).authenticated()

            .requestMatchers(
                    SecurityPath.MY_PAGE.getPath(),
                    SecurityPath.ITEM_INFO.getPath(),
                    SecurityPath.NOTICE_INFO.getPath(),
                    SecurityPath.API.getPath()
            ).hasAnyAuthority("ROLE_USER", "ROLE_ADMIN")

            .anyRequest().permitAll();
    }

    private void configureFormLogin(FormLoginConfigurer<HttpSecurity> formLogin) throws RuntimeException {
        formLogin
            .loginPage(SecurityPath.LOGIN.getPath()) // The URL to the login page
            .defaultSuccessUrl(SecurityPath.LIST.getPath(), true)
            .failureUrl(SecurityPath.LOGIN.getPath() + "?error=true")
            .permitAll();
    }

    private void configureLogout(LogoutConfigurer<HttpSecurity> logout) throws RuntimeException {
        logout
            .logoutUrl(SecurityPath.LOGOUT.getPath())
            .logoutSuccessUrl(SecurityPath.LIST.getPath()) // redirect to Item List page
            .permitAll();
    }

    private void configureExceptionHandling(ExceptionHandlingConfigurer<HttpSecurity> exception) {
        exception
            .authenticationEntryPoint(((request, response, authException) -> {
               if (request.getRequestURI().equals(SecurityPath.API_CHECK_AUTH.getPath())) {
                   response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
               } else {
                   customAuthEntryPoint.commence(request, response, authException);
               }
            }))
            // 401, 403
            .authenticationEntryPoint(customAuthEntryPoint)
            // 404
            .accessDeniedHandler(customAccessDeniedHandler());
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

package com.personal.shop.config.module;

import com.personal.shop.component.CustomAuthEntryPoint;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@RequiredArgsConstructor
public class ExceptionHandlingSecurityConfig {

    private final CustomAuthEntryPoint customAuthEntryPoint;

    public void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling(this::configureExceptionHandling);
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
    public AccessDeniedHandler customAccessDeniedHandler() {

        return ((request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write("엑세스 거부 : 권한이 없습니다.");
        });
    }
}

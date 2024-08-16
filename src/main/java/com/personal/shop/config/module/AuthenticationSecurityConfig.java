package com.personal.shop.config.module;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;

@Configuration
@RequiredArgsConstructor
public class AuthenticationSecurityConfig {

    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(this::configureAuthorizeHttpRequests) // Authentication
            .formLogin(this::configureFormLogin)
            .logout(this::configureLogout);
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
}

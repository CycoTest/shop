package com.personal.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

@Slf4j
@Getter
@Setter
public class CustomUser extends User {
    private String displayName;

    public CustomUser(String username,
                      String password,
                      Collection<? extends GrantedAuthority> authorities,
                      String displayName) {

        // 검증된 값으로 부모 클래스 생성자 호출
        super(validateUsername(username), validatePassword(password), validateAuthorities(authorities));
        this.displayName = displayName;
        log.debug("CustomUser created with username: {}, authorities: {}, displayName: {}",
                username, authorities, displayName);

    }

    private static String validateUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be null or empty");
        }

        return username;
    }

    private static String validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        return password;
    }

    private static Collection<? extends GrantedAuthority> validateAuthorities(Collection<? extends GrantedAuthority> authorities) {

        return authorities == null ? Collections.emptyList() : authorities;
    }


}

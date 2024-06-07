package com.personal.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
@Setter
public class CustomUser extends User {
    private String displayName;

    public CustomUser(String username,
                      String password,
                      Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }


}

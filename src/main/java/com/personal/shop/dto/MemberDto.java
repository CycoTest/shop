package com.personal.shop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberDto {
    private String username;
    private String displayName;

    public MemberDto(String username, String displayName) {
        this.username = username;
        this.displayName = displayName;
    }
}

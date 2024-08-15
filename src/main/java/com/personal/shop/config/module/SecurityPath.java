package com.personal.shop.config.module;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SecurityPath {

    LOGIN("/login"),
    REGISTER("/register"),
    LOGOUT("/logout"),

    API_CHECK_AUTH("/api/check_auth"),
    LIST("/list"),

    ACTUATOR("/actuator/**"),
    API("/api/**"),

    ITEM_INFO("/itemInfo/**"),
    NOTICE_INFO("noticeInfo/**"),
    MY_PAGE("/mayPage/**"),
    DETAIL("/detail/**");

    private final String path;
}

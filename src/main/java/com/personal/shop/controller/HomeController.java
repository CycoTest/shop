package com.personal.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    String goHome() {

        return "index";
    }

    @GetMapping("/about")
    String goAbout() {

        return "테스트 중이에요";
    }
}

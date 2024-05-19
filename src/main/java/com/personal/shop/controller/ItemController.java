package com.personal.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {

    @GetMapping("/list")
    String showList(Model model) {
        model.addAttribute("name", "비싼 바지");

        return "index.html";
    }
}

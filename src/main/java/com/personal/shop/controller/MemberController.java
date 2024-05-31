package com.personal.shop.controller;

import com.personal.shop.entity.Member;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class MemberController {


    @GetMapping("/member")
    String goLogInForm() {

        return "base/members/logIn";
    }

    @PostMapping("/member/login")
    String enterMember(String username, String password)  {
        Member member = new Member();
        if (member.getUserName().equals(username)) {
            if (member.getPassword().equals(password)) {

                return "redirect:/list";
            }
        }

        return "redirect:/member";
    }



}

package com.personal.shop.controller;

import com.personal.shop.entity.CustomUser;
import com.personal.shop.repository.MemberRepository;
import com.personal.shop.service.MemberService;
import com.personal.shop.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final MyUserDetailsService myUserDetailsService;

    @GetMapping("/member")
    String showLoginForm() {

        return "members/animatedLogin";
    }

    @PostMapping("/register")
    String beMember(@RequestParam("sign_username") String username,
                    @RequestParam("sign_password") String password,
                    @RequestParam("sign_displayName") String displayName)
            throws Exception {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth.isAuthenticated()) {
            return "redirect:/list";
        }
        memberService.saveMember(username, password, displayName);

        return "redirect:/member";
    }

    @GetMapping("/myPage")
    String goMyPage(Authentication auth) {
        CustomUser result = (CustomUser) auth.getPrincipal();
        System.out.println(result.getDisplayName());

        return "members/myPage";
    }


}

package com.personal.shop.controller;

import com.personal.shop.entity.Member;
import com.personal.shop.service.MemberService;
import com.personal.shop.service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final MyUserDetailsService myUserDetailsService;

    @GetMapping("/member")
    String showLoginForm() {

        return "members/animatedLogin";
    }

    @PostMapping("/member/login")
    String enterMember(@RequestParam("log_username") String username,
                       @RequestParam("log_password") String password)  {

        System.out.println(username);
        System.out.println(password);
        myUserDetailsService.loadUserByUsername(username);

        return "redirect:/member";
    }

    @PostMapping("/member/register")
    String beMember(@RequestParam("sign_username") String username,
                    @RequestParam("sign_password") String password,
                    @RequestParam("sign_displayName") String displayName)
            throws Exception {

        memberService.saveMember(username, password, displayName);

        return "redirect:/member";
    }

}

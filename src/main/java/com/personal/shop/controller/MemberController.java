package com.personal.shop.controller;

import com.personal.shop.entity.Member;
import com.personal.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member")
    String showLoginForm() {

        return "members/animatedLogin";
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

    @PostMapping("/member/register")
    String beMember(@RequestParam("su_username") String username,
                    @RequestParam("su_password") String password,
                    @RequestParam("su_displayName") String displayName)
            throws Exception {

        memberService.saveMember(username, password, displayName);

        return "redirect:/member";
    }

}

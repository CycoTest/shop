package com.personal.shop.controller;

import com.personal.shop.dto.MemberDto;
import com.personal.shop.entity.CustomUser;
import com.personal.shop.entity.Member;
import com.personal.shop.repository.MemberRepository;
import com.personal.shop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final MemberService memberService;

    @GetMapping("/login")
    String showLoginForm() {

        return "members/animatedLogin";
    }

    @PostMapping("/register")
    String beMember(@RequestParam("sign_username") String username,
                    @RequestParam("sign_password") String password,
                    @RequestParam("sign_displayName") String displayName)
            throws Exception {

        memberService.saveMember(username, password, displayName);

        return "redirect:/login";
    }

    @GetMapping("/myPage")
    String goMyPage(Authentication auth) {
        CustomUser result = (CustomUser) auth.getPrincipal();
        System.out.println(result.getDisplayName());

        return "members/myPage";
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    public MemberDto getUser(@PathVariable Long id) {
        Optional<Member> a = memberRepository.findById(id);
        if (a.isPresent()) {
            Member result = a.get();

            return new MemberDto(result.getUserName(), result.getDisplayName());
        }

        return null;
    }


}

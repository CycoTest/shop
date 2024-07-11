package com.personal.shop.controller;

import com.personal.shop.dto.MemberDto;
import com.personal.shop.entity.CustomUser;
import com.personal.shop.entity.Member;
import com.personal.shop.repository.MemberRepository;
import com.personal.shop.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    String showMyPage(Model model, Authentication auth, HttpSession session) {
        CustomUser user = (CustomUser) auth.getPrincipal();

        // session에서 로그인 확인 상태 체크
        Boolean isVerified = (Boolean) session.getAttribute("isVerified");
        if (isVerified == null || !isVerified) {

            return "redirect:/myPage/check";
        }

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "마이 페이지");
        model.addAttribute("showSideBar", true);

        // 로그인 확인 상태 초기화
        session.removeAttribute("isVerified");

        return "members/myPage";
    }

    @GetMapping("/myPage/check")
    String checkMyPage(Authentication auth, Model model) {
        CustomUser user = (CustomUser) auth.getPrincipal();

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "마이 페이지 로그인");
        model.addAttribute("showSidebar", false);

        return "members/myPageCheck";
    }

    @PostMapping("/myPage/verify")
    String verifyUser(@RequestParam("password") String password,
                      Authentication auth,
                      HttpSession session,
                      RedirectAttributes redirectAttributes) {
        CustomUser user = (CustomUser) auth.getPrincipal();
        if (memberService.verifyUser(user.getUsername(), password)) {
            // 비밀번호가 일치, 유저 정보를 세션에 저장하고 myPage/info로 리다이렉트
            session.setAttribute("isVerified", true);
            redirectAttributes.addFlashAttribute("passedUser", user);

            return "redirect:/myPage";
        } else {
            // 비밀번호 불일치, 에러 메세지와 다시 myPage로 리다이렉트
            redirectAttributes.addFlashAttribute("error", "비밀번호가 일치하지 않습니다.");

            return "redirect:/myPage/check";
        }
    }


}

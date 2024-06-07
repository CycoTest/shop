package com.personal.shop.service;

import com.personal.shop.entity.Member;
import com.personal.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveMember(String username, String password, String displayName)
            throws Exception {

        if (username.length() < 8 || password.length() < 8) {
            throw new Exception("아이디 혹은 비밀번호가 너무 짧음");
        }

        Member member = new Member();
        member.setUserName(username);
        member.setDisplayName(displayName);
        String pwHash = passwordEncoder.encode(password);
        member.setPassword(pwHash);

        // Check displayName and assign roles accordingly
        List<String> roles = new ArrayList<>();
        if ("admin".equalsIgnoreCase(displayName)) {
            roles.add("ROLE_ADMIN");
        } else {
            roles.add("ROLE_USER");
        }
        member.setRoles(roles);

        memberRepository.save(member);
    }
}

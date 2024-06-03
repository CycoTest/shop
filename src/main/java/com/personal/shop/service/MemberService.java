package com.personal.shop.service;

import com.personal.shop.entity.Member;
import com.personal.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void saveMember(String username, String displayName, String password)
            throws Exception {

        if (username.length() < 8 || password.length() < 8) {
            throw new Exception("너무 짧음");
        }

        Member member = new Member();
        member.setUserName(username);
        member.setDisplayName(displayName);
        String pwHash = passwordEncoder.encode(password);
        member.setPassword(pwHash);

        memberRepository.save(member);
    }
}

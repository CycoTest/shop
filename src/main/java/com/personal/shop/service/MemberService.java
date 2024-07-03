package com.personal.shop.service;

import com.personal.shop.entity.Member;
import com.personal.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
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

        System.out.println(member);
        log.info("Saving member: {}", member);

        memberRepository.save(member);
    }
}

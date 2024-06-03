package com.personal.shop.service;

import com.personal.shop.entity.Member;
import com.personal.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB 에서 username 을 가진 유저들 찾아서,
        // return new User(유저_아이디, 비번, 권한)

        Optional<Member> result = memberRepository.findByUserName(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
        }

        var user = result.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("일반유저"));
        System.out.println("로그인 성공");

        return new User(user.getUserName(), user.getPassword(), authorities);
    }
}

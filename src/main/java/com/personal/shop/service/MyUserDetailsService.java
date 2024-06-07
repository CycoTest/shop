package com.personal.shop.service;

import com.personal.shop.entity.CustomUser;
import com.personal.shop.entity.Member;
import com.personal.shop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    @Transactional // Ensure a transaction is active
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB 에서 username 을 가진 유저들 찾아서,
        // return new User(유저_아이디, 비번, 권한)

        Optional<Member> result = memberRepository.findByUserName(username);
        if (result.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
        }

        Member user = result.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getDisplayName().equalsIgnoreCase("admin")) {
            authorities.add(new SimpleGrantedAuthority("관리자"));
        } else {
            authorities.add(new SimpleGrantedAuthority("일반유저"));
        }

        // displayName 데이터를 추가하기 위해, userdetails의 User 클래스를 상속받은 CustomUser 클래스를 만들어줌
        // 그런 다음 displayName 변수를 추가하여, 로그인한 유저의 정보를 불러와 저장함
        CustomUser customUser = new CustomUser(user.getUserName(), user.getPassword(), authorities);
        customUser.setDisplayName(user.getDisplayName());

        return customUser;
    }
}

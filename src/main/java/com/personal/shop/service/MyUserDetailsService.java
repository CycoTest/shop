package com.personal.shop.service;

import com.personal.shop.entity.CustomUser;
import com.personal.shop.entity.Member;
import com.personal.shop.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional // Ensure a transaction is active
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // DB 에서 username 을 가진 유저들 찾아서
        log.debug("Loading user by username: {}", username);

        Optional<Member> result = memberRepository.findByUserName(username);
        if (result.isEmpty()) {
            log.warn("User not found: {}", username);
            throw new UsernameNotFoundException("존재하지 않는 아이디입니다.");
        }

        Member user = result.get();
        log.debug("User found: {}", username);

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getDisplayName().equalsIgnoreCase("admin")) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        log.debug("Creating CustomUser with username: {}, password: {}, authorities: {}, displayName: {}",
                user.getUserName(), "****", authorities, user.getDisplayName());

        // displayName 데이터를 추가하기 위해, userdetails의 User 클래스를 상속받은 CustomUser 클래스를 만들어줌
        // 그런 다음 displayName 변수를 추가하여, 로그인한 유저의 정보를 불러와 저장함

        // CustomUser 생성자에 직접 displayName 전달
        return new CustomUser(
                user.getUserName(),
                user.getPassword(),
                authorities,
                user.getDisplayName()
        );
    }
}

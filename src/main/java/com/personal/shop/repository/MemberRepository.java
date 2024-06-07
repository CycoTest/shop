package com.personal.shop.repository;

import com.personal.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

   Optional<Member> findByUserName(String username);
}

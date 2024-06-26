package com.personal.shop.repository;

import com.personal.shop.entity.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    Slice<Notice> findSliceBy(Pageable page);
}

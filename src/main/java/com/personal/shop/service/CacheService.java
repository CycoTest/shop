package com.personal.shop.service;

import com.personal.shop.repository.ItemRepository;
import com.personal.shop.repository.NoticeRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Getter
@RequiredArgsConstructor
public class CacheService {

    private final ItemRepository itemRepository;
    private final NoticeRepository noticeRepository;

    private long itemCount;
    private long noticeCount;

    @PostConstruct
    public void initializeCounts() {
        this.itemCount = itemRepository.count();
        this.noticeCount = noticeRepository.count();
    }

    public void updateItemCount(long itemCount) {

        this.itemCount = itemCount;
    }

    public void updateNoticeCount(long noticeCount) {

        this.noticeCount = noticeCount;
    }
}

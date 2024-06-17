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

    // 상품 게시판의 총 페이지가 추가되거나 줄어들었을 때 사용
    public void updateItemCount(long newItemCount) {

        this.itemCount = newItemCount;
    }

    public void increaseItemCount() {
        this.itemCount++;
    }

    public void decreaseItemCount() {
        this.itemCount--;
    }

    // 공지사항 게시판의 총 페이지가 추가되거나 줄어들었을 때 사용
    public void updateNoticeCount(long noticeCount) {

        this.noticeCount = noticeCount;
    }
}

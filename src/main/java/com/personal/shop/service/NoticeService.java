package com.personal.shop.service;

import com.personal.shop.entity.Notice;
import com.personal.shop.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;
    private final PaginationService paginationService;
    private final CacheService cacheService;

    public Slice<Notice> getNoticeSlice(int page, int size) {
        Pageable pageable = paginationService.createPageable(page, size);

        return noticeRepository.findSliceBy(pageable);
    }

    public Map<String, Object> getPageData(int page, int size) {
        long totalNotice = cacheService.getNoticeCount();

        return paginationService.getPageData(page, size, totalNotice);
    }

    public void saveItem(String title, String contents, String author) {
        Notice notice = new Notice();
        notice.setTitle(title);
        notice.setContents(contents);
        notice.setAuthor(author);
        notice.setPostDate(LocalDate.now());

        noticeRepository.save(notice);
    }
}

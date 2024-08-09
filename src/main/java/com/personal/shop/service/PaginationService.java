package com.personal.shop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaginationService {

    public Pageable createPageable(int page, int size) {

        return PageRequest.of(page, size);
    }

    public Map<String, Object> getPageData(int page, int size, long totalItems) {
        int totalPages = calculateTotalPages(size, totalItems);
        int currentPage = page + 1;

        // 숫자 묶음 계산
        int pagesPerBlock = 5;
        int currentBlock = (int) Math.ceil((double) currentPage / pagesPerBlock);

        // 첫 장과 끝 장 계산
        int startPage = (currentBlock - 1) * pagesPerBlock + 1;
        int endPage = Math.min(currentBlock * pagesPerBlock, totalPages);

        Map<String, Object> pageData = new HashMap<>();
        pageData.put("totalPages", totalPages);
        pageData.put("currentPage", currentPage);
        pageData.put("startPage", startPage);
        pageData.put("endPage", endPage);
        pageData.put("size", size);

        return pageData;
    }

    private int calculateTotalPages(int size, long totalItems) {

        return (int) Math.ceil((double) totalItems / size);
    }
}

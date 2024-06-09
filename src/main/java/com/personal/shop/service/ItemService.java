package com.personal.shop.service;

import com.personal.shop.entity.Item;
import com.personal.shop.repository.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private int totalPages = 0;

    // prototype of showList method
    public List<Item> bringItemList() {

        return itemRepository.findAll();
    }

    public Optional<Item> bringItemById(Long id) {

        return itemRepository.findById(id);
    }

    public void saveItem(String title, Integer price, String registerUser) {
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setRegisterUser(registerUser);

        itemRepository.save(item);
    }

    public void saveItemById(Long id, String title, Integer price) {
        Item item = new Item();
        item.setId(id);

        if (title.length() > 255) {

        } else {
            item.setTitle(title);
        }

        item.setPrice(price);

        itemRepository.save(item);
    }

    public Optional<Item> editItem(Long id) {

        return itemRepository.findById(id);
    }

    public void eraseItem(Long id) {

        itemRepository.deleteById(id);
    }

    // pagination process in showList method
    public Map<String, Object> getPageData(int page, int size) {
        int totalPages = getTotalPages(size);
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

    @PostConstruct
    public void initializeTotalPages() {
        int size = 5;
        this.totalPages = calculateTotalPages(size);
    }

    public int calculateTotalPages(int size) {
        long totalItems = itemRepository.count();

        return (int) Math.ceil((double) totalItems / size);
    }

    public int getTotalPages(int size) {
        return this.totalPages;
    }

    public Slice<Item> getItemsSlice(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return itemRepository.findSliceBy(pageable);
    }
}

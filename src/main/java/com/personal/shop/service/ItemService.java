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
    private final PaginationService paginationService;
    private final CacheService cacheService;

    // prototype of showList method
    public List<Item> bringItemList() {

        return itemRepository.findAll();
    }

    public Optional<Item> bringItemById(Long id) {

        return itemRepository.findById(id);
    }

    public void saveItem(String title, Integer price,
                         String registerUser, String imageURL) {
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);
        item.setRegisterUser(registerUser);
        item.setImageUrl(imageURL);

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

    public int calculateTotalPages(int size) {
        long totalItems = itemRepository.count();

        return (int) Math.ceil((double) totalItems / size);
    }

    public Slice<Item> getItemsSlice(int page, int size) {
        Pageable pageable = paginationService.createPageable(page, size);

        return itemRepository.findSliceBy(pageable);
    }

    public Map<String, Object> getPageData(int page, int size) {
        long totalItems = cacheService.getItemCount();

        return paginationService.getPageData(page, size, totalItems);
    }
}

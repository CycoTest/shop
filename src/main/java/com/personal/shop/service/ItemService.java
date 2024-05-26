package com.personal.shop.service;

import com.personal.shop.entity.Item;
import com.personal.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public List<Item> bringItemList() {

        return itemRepository.findAll();
    }

    public void saveItem(String title, Integer price) {
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);

        itemRepository.save(item);
    }



    public Optional<Item> bringItemById(Long id) {

        return itemRepository.findById(id);
    }
}
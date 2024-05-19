package com.personal.shop.controller;

import com.personal.shop.entity.Item;
import com.personal.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;

    @GetMapping("/list")
    String showList(Model model) {
        List<Item> result = itemRepository.findAll();
        System.out.println(result);

        model.addAttribute("items", result);

        return "list.html";
    }

    @GetMapping("/write")
    String showWriteForm() {

        return "write.html";
    }

    @PostMapping("/itemInfo")
    String addItem(String title, Integer price) {
        // 파라미터에 @ModelAttribute Item item 으로 해도 됨
        // 대신 이렇게 할 경우, itemRepository.save(item); 만 쓰면 됨
        Item item = new Item();
        item.setTitle(title);
        item.setPrice(price);

        itemRepository.save(item);

        return "redirect:/list";
    }
}

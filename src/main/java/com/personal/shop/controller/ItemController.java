package com.personal.shop.controller;

import com.personal.shop.entity.Item;
import com.personal.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
}

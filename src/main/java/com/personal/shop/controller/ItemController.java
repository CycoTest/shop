package com.personal.shop.controller;

import com.personal.shop.entity.Item;
import com.personal.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/detail/{id}")
    String viewItemDetail(@PathVariable Long id, Model model) {
        Optional<Item> result = itemRepository.findById(id);
        if (result.isPresent()) {
            model.addAttribute("itemDetail", result.get());
            System.out.println(result.get());

            return "detail.html";
        } else {

            return "redirect:/list";
        }

        // 에러 코드
        // 유저 잘못 = 4XX, 서버 잘못 = 5XX, 정상 작동 = 200
    }

}

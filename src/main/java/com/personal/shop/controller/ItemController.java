package com.personal.shop.controller;

import com.personal.shop.entity.Item;
import com.personal.shop.repository.ItemRepository;
import com.personal.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    @GetMapping("/list")
    String showList(Model model) {

        model.addAttribute("items", itemService.bringItemList());

        return "list.html";
    }

    @GetMapping("/itemInfo/write")
    String showItemWriteForm() {

        return "write.html";
    }

    @PostMapping("/itemInfo/write")
    String addItem(String title, Integer price) {
        // 파라미터에 @ModelAttribute Item item 으로 해도 됨
        // 대신 이렇게 할 경우, itemRepository.save(item); 만 쓰면 됨
        // 하지만 함수 하나당 기능은 하나씩만 저장하는 게 낫기 때문에
        // 비지니스 로직은 service 로 뺌
        itemService.saveItem(title, price);

        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String viewItemDetail(@PathVariable Long id, Model model) {

        Optional<Item> result = itemService.bringItemById(id);
        if (result.isPresent()) {
            model.addAttribute("itemDetail", result.get());
//            System.out.println(result.get());

            return "detail.html";
        } else {

            return "redirect:/list";
        }

        // 에러 코드
        // 유저 잘못 = 4XX, 서버 잘못 = 5XX, 정상 작동 = 200
    }
    
}

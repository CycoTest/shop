package com.personal.shop.controller;

import com.personal.shop.entity.Item;
import com.personal.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/list")
    String showList(Model model) {

        List<Item> result = itemService.bringItemList();
        model.addAttribute("items", result);

        return "base/list";
    }

    @GetMapping("/itemInfo/write")
    String showItemWriteForm() {

        return "detail/write";
    }

    @PostMapping("/itemInfo/write")
    String addItem(String title, Integer price) {
        // 파라미터에 @ModelAttribute Item item 으로 해도 됨
        // 대신 이렇게 할 경우, itemRepository.save(item); 만 쓰면 됨
        // 하지만 함수 하나당 기능은 하나씩만 저장하는 게 낫기 때문에
        // 비지니스 로직은 service 로 뺌
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/member";
        }

        itemService.saveItem(title, price);

        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String viewItemDetail(@PathVariable Long id, Model model) {

        Optional<Item> result = itemService.bringItemById(id);
        if (result.isPresent()) {
            model.addAttribute("itemDetail", result.get());
//            System.out.println(result.get());

            return "detail/detail";
        } else {

            return "redirect:/list";
        }

        // 에러 코드
        // 유저 잘못 = 4XX, 서버 잘못 = 5XX, 정상 작동 = 200
    }

    @GetMapping("/itemInfo/{id}")
    String showEditItemForm(@PathVariable Long id, Model model) {

        Optional<Item> result = itemService.editItem(id);
        if (result.isPresent()) {
            model.addAttribute("itemData", result.get());

            return "detail/edit";
        } else {

            return "redirect:/list";
        }
    }

    @PostMapping("/itemInfo/edit/{id}")
    String updateItem(@PathVariable Long id, String title, Integer price) {

        Optional<Item> result = itemService.bringItemById(id);
        if (result.isPresent()) {
            itemService.saveItemById(id, title, price);

            return "redirect:/list";
        } else {

            return "error/error";
        }
    }

//    @PostMapping("/test1")
//    String test1(@RequestBody Map<String, Object> body) {
//        System.out.println("요청 들어옴");
//        System.out.println("body 값은 " + body);
//        System.out.println("body 에 저장된 name 값은 " + body.get("name"));
//
//        return "redirect:/list";
//    }

    @DeleteMapping("/itemInfo/delete")
    ResponseEntity<String> doRemoveItem(@RequestParam Long id) {

        itemService.eraseItem(id);

        return ResponseEntity.status(200).body("삭제 완료");
    }
}

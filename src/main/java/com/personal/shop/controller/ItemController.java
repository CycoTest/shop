package com.personal.shop.controller;

import com.personal.shop.entity.Item;
import com.personal.shop.repository.ItemRepository;
import com.personal.shop.service.AswS3Service;
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

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final AswS3Service aswS3Service;

    // Slice 클래스를 활용한 페이징 처리
    @GetMapping("/list")
    String showList(Model model,
                    @RequestParam(defaultValue = "0") Integer page) {

        int size = 5;
        List<Item> items = itemService.getItemsSlice(page, size).getContent();
        Map<String, Object> pageData = itemService.getPageData(page, size);

        model.addAttribute("items", items);
        model.addAttribute("pageData", pageData);

        return "base/list";
    }

    @GetMapping("/itemInfo/write")
    String showItemWriteForm() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/member";
        }

        return "detail/write";
    }

    @PostMapping("/itemInfo/write")
    String addItem(@RequestParam("title") String title,
                   @RequestParam("price") Integer price,
                   @RequestParam("uploader") String registerUser) {
        // 파라미터에 @ModelAttribute Item item 으로 해도 됨
        // 대신 이렇게 할 경우, itemRepository.save(item); 만 쓰면 됨
        // 하지만 함수 하나당 기능은 하나씩만 저장하는 게 낫기 때문에
        // 비지니스 로직은 service 로 뺌
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/member";
        }

        itemService.saveItem(title, price, registerUser);

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/member";
        }

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
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "redirect:/member";
        }

        Optional<Item> result = itemService.bringItemById(id);
        if (result.isPresent()) {
            itemService.saveItemById(id, title, price);

            return "redirect:/list";
        } else {

            return "error/error";
        }
    }

    @DeleteMapping("/itemInfo/delete")
    ResponseEntity<String> doRemoveItem(@RequestParam Long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Unauthorized");
        }

        itemService.eraseItem(id);

        return ResponseEntity.status(200).body("삭제 완료");
    }

    @GetMapping("/presigned-url")
    @ResponseBody
    String getURL(@RequestParam String filename) {
        System.out.println(filename);
        String result = aswS3Service.createPreSignedUrl("test/" + filename);
        System.out.println(result);

        return result;
    }

}

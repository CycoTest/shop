package com.personal.shop.controller;

import com.personal.shop.entity.CustomUser;
import com.personal.shop.entity.Item;
import com.personal.shop.repository.ItemRepository;
import com.personal.shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

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
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    String showItemWriteForm() {

        return "detail/item/itemWrite";
    }

    @PostMapping("/itemInfo/write")
    String addItem(@RequestParam("title") String title,
                   @RequestParam("price") Integer price,
                   @RequestParam("uploader") String registerUser,
                   @RequestParam(value = "imageURL", required = false) String imageURL) {
        // 파라미터에 @ModelAttribute Item item 으로 해도 됨
        // 대신 이렇게 할 경우, itemRepository.save(item); 만 쓰면 됨
        // 하지만 함수 하나당 기능은 하나씩만 저장하는 게 낫기 때문에
        // 비지니스 로직은 service 로 뺌

        itemService.saveItem(title, price, registerUser, imageURL);

        return "redirect:/list";
    }

    @GetMapping("/detail/{id}")
    String viewItemDetail(@PathVariable Long id, Model model) {

        Optional<Item> result = itemService.bringItemById(id);
        if (result.isPresent()) {
            model.addAttribute("itemDetail", result.get());
//            System.out.println(result.get());

            return "detail/item/itemDetail";
        } else {

            return "redirect:/list";
        }

        // 에러 코드
        // 유저 잘못 = 4XX, 서버 잘못 = 5XX, 정상 작동 = 200
    }

    // 수정 폼 페이지 Read
    @GetMapping("/itemInfo/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    String showEditItemForm(@PathVariable Long id,
                            Model model,
                            RedirectAttributes redirectAttributes,
                            Authentication auth) {
        // 유저가 선택한 item 조회
        Optional<Item> result = itemService.editItem(id);
        if (result.isPresent()) {
            Item item = result.get();
            CustomUser customUser = (CustomUser) auth.getPrincipal();
            // 현재 유저와 item 업로더가 일치하지 않거나 관리자가 아닌 경우
            if (!item.getRegisterUser().equals(customUser.getDisplayName()) && !customUser.getDisplayName().equalsIgnoreCase("admin")) {
                redirectAttributes.addFlashAttribute("errorMessage", "수정할 권한이 없습니다.");

                return "redirect:/list";
            }

            // item 수정 페이지로 리디렉션 url 반환
            model.addAttribute("itemData", item);
            return "detail/item/itemEdit";

        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "상품을 찾을 수 없습니다.");

            return "redirect:/list";
        }
    }

    // item 수정 Update
    @PostMapping("/itemInfo/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    String updateItem(@PathVariable Long id, String title, Integer price) {
        // item 조회
        Optional<Item> result = itemService.bringItemById(id);
        if (result.isPresent()) {
            itemService.saveItemById(id, title, price);

            return "redirect:/list";
        } else {

            return "error/error";
        }
    }

    @DeleteMapping("/itemInfo/delete")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    ResponseEntity<String> doRemoveItem(@RequestParam Long id, Authentication auth) {

        CustomUser customUser = (CustomUser) auth.getPrincipal();
        Optional<Item> itemOptional = itemService.bringItemById(id);
        if (itemOptional.isEmpty()) {

            return ResponseEntity.status(404).body("상품을 찾을 수 없습니다.");
        }

        Item item = itemOptional.get();
        if (!item.getRegisterUser().equals(customUser.getDisplayName()) && !customUser.getDisplayName().equalsIgnoreCase("admin")) {

            return ResponseEntity.status(403).body("삭제 권한이 없습니다.");
        }

        itemService.eraseItem(id);

        return ResponseEntity.status(200).body("삭제가 완료됐습니다.");
    }
}

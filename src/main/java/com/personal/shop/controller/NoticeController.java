package com.personal.shop.controller;

import com.personal.shop.entity.Notice;
import com.personal.shop.service.AwsS3Service;
import com.personal.shop.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;

    @GetMapping("/notice")
    String showNotice(Model model,
                      @RequestParam(defaultValue = "0") Integer page) {

        int size = 5;
        List<Notice> notices = noticeService.getNoticeSlice(page, size).getContent();
        Map<String, Object> pageData = noticeService.getPageData(page, size);

        model.addAttribute("notices", notices);
        model.addAttribute("pageData", pageData);

        return "base/notice";
    }

    @GetMapping("/noticeInfo/write")
    String showNoticeWriteForm() {

        return "detail/notice/noticeWrite";
    }

    @PostMapping("/noticeInfo/write")
    String addItem(@RequestParam("title") String title,
                   @RequestParam("contents") String contents,
                   @RequestParam("author") String author) {
        // 파라미터에 @ModelAttribute Item item 으로 해도 됨
        // 대신 이렇게 할 경우, itemRepository.save(item); 만 쓰면 됨
        // 하지만 함수 하나당 기능은 하나씩만 저장하는 게 낫기 때문에
        // 비지니스 로직은 service 로 뺌

        noticeService.saveItem(title, contents, author);

        return "redirect:/notice";
    }
}

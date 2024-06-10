package com.personal.shop.controller;

import com.personal.shop.entity.Notice;
import com.personal.shop.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

}

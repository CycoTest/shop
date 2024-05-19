package com.personal.shop.controller;

import com.personal.shop.entity.Notice;
import com.personal.shop.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeRepository noticeRepository;

    @GetMapping("/notice")
    String showNotice(Model model) {
        List<Notice> result = noticeRepository.findAll();
        System.out.println(result);

        model.addAttribute("notices", result);

        return "notice.html";
    }
}

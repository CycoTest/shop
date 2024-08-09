package com.personal.shop.controller;

import com.personal.shop.entity.Comment;
import com.personal.shop.entity.CustomUser;
import com.personal.shop.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentRepository commentRepository;

    @PostMapping("/comment")
    String attachComment(@RequestParam String content,
                         @RequestParam Long parent,
                         @RequestParam Double rating,
                         Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();

        Comment data = new Comment();
        data.setContent(content);
        data.setParentId(parent);
        data.setDisplayName(user.getDisplayName());
        data.setRating(rating);
        commentRepository.save(data);

        // 댓글을 단 상세 페이지로 리다이렉트
        return "redirect:/detail/" + parent;
    }
}

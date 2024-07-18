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
                         Authentication auth) {
        CustomUser user = (CustomUser) auth.getPrincipal();

        Comment data = new Comment();
        data.setContent(content);
        data.setParentId(parent);
        data.setUsername(user.getUsername());
        commentRepository.save(data);

        return "detail/item/itemDetail";
    }
}

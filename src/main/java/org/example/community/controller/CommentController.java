package org.example.community.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.community.domain.Comment;
import org.example.community.domain.User;
import org.example.community.dto.CommentWriteDto;
import org.example.community.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/write")
    public String writeComment(@ModelAttribute CommentWriteDto dto, HttpSession session){
        User loginUser = (User)session.getAttribute("loginUser");

        Comment comment = new Comment();
        comment.setPostId(dto.getPostId());
        comment.setParentId(dto.getParentId());
        comment.setComments(dto.getComments());
        comment.setAuthorId(loginUser.getId());

        commentService.writeComment(comment);

        return "redirect:/post/" + dto.getPostId();
    }

    // 댓글 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @RequestParam Long postId) {
        commentService.deleteComment(id);
        return "redirect:/post/" + postId;
    }
}

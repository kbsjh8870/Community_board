package org.example.community.service;

import lombok.RequiredArgsConstructor;
import org.example.community.domain.Comment;
import org.example.community.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 목록
    public List<Comment> findCommentsByPostId(Long postId){
        return commentRepository.findByPostIdOrderByCreatedAt(postId);
    }

    // 댓글 추가
    public Comment writeComment (Comment comment){
        comment.setCreatedAt(LocalDateTime.now());
        return commentRepository.save(comment);
    }

    // 댓글 삭제
    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }

}

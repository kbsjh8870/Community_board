package org.example.community.service;

import lombok.RequiredArgsConstructor;
import org.example.community.domain.Comment;
import org.example.community.dto.CommentWithRepliesDto;
import org.example.community.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 목록
    public List<CommentWithRepliesDto> findCommentsByPostId(Long postId){
        List<Comment> allComments = commentRepository.findByPostIdOrderByCreatedAt(postId);

        // 원 댓글
        List<Comment> originComment = allComments.stream()
                .filter(comment -> comment.getParentId()==null)
                .toList();

        // 원 댓글 List => (원댓글,대댓글 List)
        List<CommentWithRepliesDto> commentWithRepliesDtos = originComment.stream()
                .map(c -> new CommentWithRepliesDto(
                        c,
                        allComments.stream()
                                .filter(comment -> comment.getParentId() !=null && comment.getParentId().equals(c.getId()))
                                .toList()
                ))
                .toList();

        return commentWithRepliesDtos;
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

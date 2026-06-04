package org.example.community.service;

import lombok.RequiredArgsConstructor;
import org.example.community.domain.Comment;
import org.example.community.dto.CommentWithRepliesDto;
import org.example.community.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public void deleteComment(Long commentId){
        commentRepository.deleteByParentId(commentId);
        commentRepository.deleteById(commentId);
    }

    // 내 댓글들 찾기
    public Page<Comment> findMyComments(Long id, Pageable pageable){
        return commentRepository.findCommentsByAuthorId(id,pageable);
    }
}

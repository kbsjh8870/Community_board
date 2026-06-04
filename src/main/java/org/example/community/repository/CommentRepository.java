package org.example.community.repository;

import org.example.community.domain.Comment;
import org.example.community.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository  extends CrudRepository<Comment,Long>, PagingAndSortingRepository<Comment,Long> {
    List<Comment> findByPostIdOrderByCreatedAt(Long postId);

    Long countByAuthorId(Long authorId);

    void deleteByParentId(Long parentId);

    Page<Comment> findCommentsByAuthorId(Long authorId, Pageable pageable);
}

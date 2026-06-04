package org.example.community.repository;

import org.example.community.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post,Long>, PagingAndSortingRepository<Post,Long> {
    /*// 최신순 전체글
    @Query("select* from post order by created_at desc")
    List<Post> findAllOrderByCreatedAtDesc();*/
    // 페이징소트로 변환

    /*// 인기글
    @Query("select * from post order by view_count desc")
    List<Post> PopularPosts();*/
    // 페이징소트로 변환

    /*// 키워드로 제목 검색
    Page<Post> findByTitleContaining(String title, Pageable pageable);*/

    Long countByAuthorId(Long authorId);
    
    Page<Post> findPostsByAuthorId(Long authorId,Pageable pageable);

}

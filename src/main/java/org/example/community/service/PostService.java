package org.example.community.service;

import lombok.RequiredArgsConstructor;
import org.example.community.domain.Post;
import org.example.community.domain.User;
import org.example.community.dto.PostListDto;
import org.example.community.repository.PostRepository;
import org.example.community.repository.PostRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostRepositoryCustom postRepositoryCustom;

    /*// 전체글
    public List<Post> findAllPosting(){
        return postRepository.findAllOrderByCreatedAtDesc();
    }*/

    /*// 전체글 - PagingSort
    public Page<Post> findAllPosting(Pageable pageable){
        return postRepository.findAll(pageable);
    }*/

    public Page<PostListDto> findAllPostingWithNickname(Pageable pageable){
        return postRepositoryCustom.findAllPostingWithNickname(pageable);
    }

    /*// 인기글 - PagingSort
    public Page<Post> popularPosting(Pageable pageable){
        return postRepository.findAll(pageable);
    }*/

    public Page<PostListDto> findPopularPostingWithNickname(Pageable pageable){
        return postRepositoryCustom.findPopularWithNickname(pageable);
    }

    /*// 글 제목으로 검색
    public Page<Post> searchPosting(String keyword,Pageable pageable){
        return postRepository.findByTitleContaining(keyword,pageable);
    }*/

    public Page<PostListDto> findTitleWithNickname(String keyword,Pageable pageable){
        return postRepositoryCustom.findByTitleWithNickname(keyword, pageable);
    }

    // 글 조회 (조회수 카운팅)
    public Post findPostingByIdAndCount(Long id, User loginUser){
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("글 조회 오류"));

        // 비로그인 or 본인글아닐 때 조회수 증가
        if (loginUser == null || !post.getAuthorId().equals(loginUser.getId())) {
            post.setViewCount(post.getViewCount() + 1);
            postRepository.save(post);
        }

        return post;
    }

    // 수정,삭제용 (노카운팅)
    public Post findPostingByIdNoCount(Long id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("글 조회 오류"));
    }

    // 글 추가
    public Post writePosting(Post post){
        post.setCreatedAt(LocalDateTime.now());
        return postRepository.save(post);
    }

    // 글 수정
    public Post updatePosting(Post post){
        return postRepository.save(post);
    }

    // 글 삭제
    public void deletePosting(Long id){
        postRepository.deleteById(id);
    }

    // 내 글들 찾기
    public Page<Post> findMyPosts(Long id, Pageable pageable){
        return postRepository.findPostsByAuthorId(id,pageable);
    }
}

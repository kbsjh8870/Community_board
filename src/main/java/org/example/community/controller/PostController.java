package org.example.community.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.community.domain.Post;
import org.example.community.domain.User;
import org.example.community.service.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 전체 글
    @GetMapping("/list")
    public String allPosts(Model model, @PageableDefault(size = 5,sort = "id",direction = Sort.Direction.ASC)Pageable pageable){
        Page<Post> posts =  postService.findAllPosting(pageable);
        model.addAttribute("posts",posts);

        return "post/list";
    }

    // 글 상세조회
    @GetMapping("/{id}")
    public String detailPost(@PathVariable Long id, Model model,HttpSession session){
        User loginUser = (User)session.getAttribute("loginUser");

        model.addAttribute("posting",postService.findPostingByIdAndCount(id,loginUser));
        return "post/detail";
    }

    // 글 작성
    @GetMapping("/write")
    public String writingForm(){
        return "post/writingForm";
    }

    @PostMapping("/write")
    public String writing(@ModelAttribute Post post, HttpSession session){
        User loginUser = (User)session.getAttribute("loginUser");

        post.setAuthorId(loginUser.getId());
        postService.writePosting(post);

        return "redirect:/post/list";
    }

    // 글 수정
    @GetMapping("/{id}/update")
    public String updatingForm(@PathVariable Long id,Model model){
        model.addAttribute("updatePost",postService.findPostingByIdNoCount(id));

        return "post/updateForm";
    }

    @PostMapping("/{id}/update")
    public String updating(@PathVariable Long id,HttpSession session,@ModelAttribute Post post){
        User loginUser = (User)session.getAttribute("loginUser");
        Post existing = postService.findPostingByIdNoCount(id);

        if(!existing.getAuthorId().equals(loginUser.getId()))
            throw new RuntimeException("수정 권한 없음");

        post.setId(id);
        post.setAuthorId(existing.getAuthorId());
        post.setViewCount(existing.getViewCount());
        post.setCreatedAt(existing.getCreatedAt());
        postService.updatePosting(post);

        return "redirect:/post/"+id;
    }

    // 글 삭제
    @GetMapping("/{id}/delete")
    public String deleting(@PathVariable Long id,HttpSession session){
        User loginUser = (User)session.getAttribute("loginUser");
        Post post = postService.findPostingByIdNoCount(id);

        if(!post.getAuthorId().equals(loginUser.getId()))
            throw new RuntimeException("삭제 권한 없음");

        postService.deletePosting(id);

        return "redirect:/post/list";
    }

    // 검색 (글 제목)
    @GetMapping("/search")
    public String search(@RequestParam String keyword,Model model,@PageableDefault(size = 5,sort = "id")Pageable pageable){
        model.addAttribute("posts",postService.searchPosting(keyword,pageable));

        return "post/list";
    }

    // 인기글
    @GetMapping("/popular")
    public String popularPostings(Model model,@PageableDefault(size = 5,sort = "viewCount",direction = Sort.Direction.DESC)Pageable pageable){
        model.addAttribute("posts",postService.popularPosting(pageable));

        return "post/list";
    }

}

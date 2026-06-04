package org.example.community.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.community.domain.Comment;
import org.example.community.domain.Post;
import org.example.community.domain.User;
import org.example.community.dto.ProfileSummaryDto;
import org.example.community.dto.UserEditDto;
import org.example.community.service.CommentService;
import org.example.community.service.PostService;
import org.example.community.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    // 회원 가입
    @GetMapping("/register")
    public String registerForm(){
        return "user/registerForm";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user){
        userService.register(user);

        return "redirect:/user/login";
    }

    // 로그인
    @GetMapping("/login")
    public String loginForm(){
        return "user/loginForm";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,@RequestParam String password, HttpSession session){
        User user = userService.login(username,password);
        session.setAttribute("loginUser",user);

        return "redirect:/post/list";
    }

    // 로그아웃
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/user/login";
    }

    // 프로필
    @GetMapping("/{userId}/profile")
    public String profile(@PathVariable("userId")Long id, Model model, @PageableDefault(size = 5,sort = "id") Pageable pageable){
        ProfileSummaryDto profileSummaryDto= userService.getUserProfileSummary(id);
        Page<Post> myPosts = postService.findMyPosts(id, pageable);
        Page<Comment> myComments = commentService.findMyComments(id,pageable);

        model.addAttribute("summary",profileSummaryDto);
        model.addAttribute("myPosts",myPosts);
        model.addAttribute("myComments",myComments);

        return "user/profile";
    }

    @GetMapping("/{userId}/editProfile")
    public String editProfileForm(@PathVariable("userId")Long id,Model model){
        User user = userService.findUserById(id);

        UserEditDto dto = new UserEditDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setNickname(user.getNickname());

        model.addAttribute("user",dto);

        return "user/editProfile";
    }

    @PostMapping("/{userId}/editProfile")
    public String editProfile(@PathVariable("userId") Long id,
                              @RequestParam String currentPW,
                              @RequestParam String newPW,
                              @RequestParam String confirmPW,
                              HttpSession session) {
        User loginUser = (User) session.getAttribute("loginUser");
        userService.updatePassword(currentPW, newPW, confirmPW, loginUser);

        // 세션 최신화
        session.setAttribute("loginUser", userService.findUserById(id));

        return "redirect:/user/" + id + "/profile";
    }

}

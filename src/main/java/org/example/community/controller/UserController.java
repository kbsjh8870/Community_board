package org.example.community.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.community.domain.User;
import org.example.community.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

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
}

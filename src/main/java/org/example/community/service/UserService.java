package org.example.community.service;

import lombok.RequiredArgsConstructor;
import org.example.community.domain.User;
import org.example.community.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원가입
    public void register(User user){
        if(!userRepository.findByUsername(user.getUsername()).isEmpty())
            throw new RuntimeException("이미 사용중인 Id");

        userRepository.save(user);
    }

    // 로그인
    public User login(String username,String password){
        return userRepository.findByUsernameAndPassword(username,password).orElseThrow(() -> new RuntimeException("로그인 실패"));
    }
}

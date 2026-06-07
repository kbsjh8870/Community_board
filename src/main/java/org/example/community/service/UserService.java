package org.example.community.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.community.domain.User;
import org.example.community.dto.ProfileSummaryDto;
import org.example.community.repository.CommentRepository;
import org.example.community.repository.PostRepository;
import org.example.community.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    // 회원가입
    public void register(User user){
        if(!userRepository.findByUsername(user.getUsername()).isEmpty())
            throw new RuntimeException("이미 사용중인 Id");

        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    // 로그인
    public User login(String username,String password){
        return userRepository.findByUsernameAndPassword(username,password).orElseThrow(() -> new RuntimeException("로그인 실패"));
    }

    public ProfileSummaryDto getUserProfileSummary(Long userId){
        User user= userRepository.findById(userId).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));

        Long postCount = postRepository.countByAuthorId(userId);
        Long commentCount = commentRepository.countByAuthorId(userId);

        return new ProfileSummaryDto(
                userId,
                user.getNickname(),
                user.getCreatedAt(),
                postCount,
                commentCount
        );
    }

    public User findUserById(Long id){
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없음"));
    }

    public void updatePassword(String currentPW, String newPW, String confirmPW, User loginUser) {
        if (!currentPW.equals(loginUser.getPassword()))
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다");
        if (currentPW.equals(newPW))
            throw new RuntimeException("새 비밀번호는 현재 비밀번호와 달라야 합니다");
        if (!newPW.equals(confirmPW))
            throw new RuntimeException("새 비밀번호와 확인 비밀번호가 다릅니다");
        if (!(newPW.length()>=2))
            throw new RuntimeException("비밀번호는 2자리 이상이여야 합니다");

        loginUser.setPassword(newPW);
        userRepository.save(loginUser);
    }
}

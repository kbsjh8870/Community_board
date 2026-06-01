package org.example.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostListDto { // 게시글 목록에서 작성자 닉네임 받기오기위한 dto
    private Long id;
    private String title;
    private String nickname;
    private int viewCount;
    private LocalDateTime createdAt;

    public String getFormattedCreatedAt() {
        if (createdAt == null) return "";
        long hours = ChronoUnit.HOURS.between(createdAt, LocalDateTime.now());
        if (hours < 24) return createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        return ChronoUnit.DAYS.between(createdAt, LocalDateTime.now()) + "일 전";
    }
}

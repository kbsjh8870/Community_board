package org.example.community.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ProfileSummaryDto {
    private Long userId;
    private String nickname;
    private LocalDateTime createdAt;
    private Long postCount;
    private Long commentCount;
}

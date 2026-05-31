package org.example.community.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("comment")
public class Comment {
    @Id
    private Long id;
    private Long postId;
    private Long authorId;
    @Column("comment") // 테이블 이름, 필드 comment로 같아서 @Column 안쓰니 오류
    private String comments;
    private LocalDateTime createdAt;

    // 작성일 포매팅
    public String getFormattedCreatedAt() {
        if (createdAt == null) return "";

        LocalDateTime now = LocalDateTime.now();
        long hours = ChronoUnit.HOURS.between(createdAt, now);

        if (hours < 24) {
            // 작성일이 하루가 지나기전 - HH:mm
            return createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            // 작성일 하루 이상 - N일 전
            long days = ChronoUnit.DAYS.between(createdAt, now);
            return days + "일 전";
        }
    }
}

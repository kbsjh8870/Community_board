package org.example.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentWriteDto {
    private Long postId;
    private Long parentId;
    private String comments;
}

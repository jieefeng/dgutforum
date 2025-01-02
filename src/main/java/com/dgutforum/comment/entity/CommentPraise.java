package com.dgutforum.comment.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentPraise {
    private Long id;
    private Long commentId;
    private Long userId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

package com.dgutforum.article.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReadHistory {
    private Long id;
    private Long userId;
    private Long articleId;
    private LocalDateTime readTime;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

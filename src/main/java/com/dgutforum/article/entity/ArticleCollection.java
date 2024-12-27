package com.dgutforum.article.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleCollection {
    private Long id;
    private Long userId;
    private Long articleId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

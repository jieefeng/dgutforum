package com.dgutforum.article.vo;

import lombok.Data;

@Data
public class PraiseVo {
    private Long userId;
    private Long articleId;
    private Long commentId;
}

package com.dgutforum.article.req;

import lombok.Data;

@Data
public class praiseVo {
    private Long userId;
    private Long articleId;
    private Long commentId;
}

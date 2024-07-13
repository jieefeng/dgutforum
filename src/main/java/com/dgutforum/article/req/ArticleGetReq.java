package com.dgutforum.article.req;

import lombok.Data;

@Data
public class ArticleGetReq {
    private Long userId;
    private Long articleId;
}

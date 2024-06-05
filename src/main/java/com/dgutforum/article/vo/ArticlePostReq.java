package com.dgutforum.article.vo;


import lombok.Data;
import java.io.Serializable;

/**
 * 发布文章请求参数
 */
@Data
public class ArticlePostReq implements Serializable {
    /**
     * 帖子ID， 当存在时，表示更新文章
     */
    private Long articleId;
    /**
     * 帖子标题
     */
    private String title;

    /**
     * 分类
     */
    private Long categoryId;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 封面
     */
    private String cover;
}
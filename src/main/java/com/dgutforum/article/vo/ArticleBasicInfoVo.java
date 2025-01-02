package com.dgutforum.article.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleBasicInfoVo {

    private Long id;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子头图
     */
    private String picture;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String photo;

    /**
     * 阅读数
     */
    private Long readCount;

    /**
     * 点赞数
     */
    private Long praise;

    /**
     * 评论数
     */
    private Long commentNumber;

    /**
     * 收藏数
     */
    private String collection;

    private LocalDateTime createTime;

    /**
     * 分类Id
     */
    private String CategoryName;

}

package com.dgutforum.article.dto;

import com.dgutforum.common.dto.BaseDO;
import lombok.Data;

@Data
public class ArticleUserDto extends BaseDO {

    /**
     * 作者
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户头像
     */
    private String photo;

    /**
     * 类目ID
     */
    private Long categoryId;

    /**
     * 帖子头图
     */
    private String picture;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子正文
     */
    private String content;

    /**
     * 评论数
     */
    private String commentNumber;

    /**
     * 点赞数
     */
    private Long praise;

    /**
     * 收藏数
     */
    private Long collection;

    /**
     * 阅读计数
     */
    private Long read_count;

    /**
     * 是否删除
     */
    private short deleted;
}

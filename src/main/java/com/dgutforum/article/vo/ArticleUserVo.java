package com.dgutforum.article.vo;

import com.dgutforum.common.dto.BaseDO;
import jdk.jfr.Category;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleUserVo extends BaseDO {

    private Long id;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子正文
     */
    private String content;

    /**
     * 帖子头图
     */
    private String picture;

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
     * 分类Id
     */
    private String CategoryId;
}

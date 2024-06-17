package com.dgutforum.article.Do;

import com.dgutforum.common.dto.BaseDO;
import lombok.Data;

@Data
public class ArticleUserDo extends BaseDO {

    /**
     * 作者
     */
    private Long userId;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子头图
     */
    private String picture;

    /**
     * 类目ID
     */
    private Long categoryId;

    /**
     * 帖子正文
     */
    private String content;

    /**
     * 点赞数
     */
    private Long praise;

    /**
     * 评论数
     */
    private Long commentNumber;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户头像
     */
    private String photo;

}

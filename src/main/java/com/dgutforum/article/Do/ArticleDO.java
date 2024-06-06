package com.dgutforum.article.Do;

import com.baomidou.mybatisplus.annotation.TableName;
import com.dgutforum.Common.dto.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 文章表

 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("article")
public class ArticleDO extends BaseDO {
    private static final long serialVersionUID = 1L;

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
    private String context;

    /**
     * 是否删除
     */
    private Integer deleted;
}

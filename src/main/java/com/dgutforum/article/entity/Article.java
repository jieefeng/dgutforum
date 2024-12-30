package com.dgutforum.article.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.dgutforum.common.dto.BaseDO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 文章信息
 * <p>
 * DTO 定义返回给web前端的实体类 (VO)
 */
@Data
public class Article extends BaseDO {

    /**
     * 作者uid
     */
    private Long userId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 封面
     */
    private String picture;

    /**
     * 正文
     */
    private String content;

    /**
     * 是否删除
     */
    private short deleted;

    /**
     * 分类
     */
    private Long categoryId;

    /**
     * 点赞
     */
    private Long praise;

    /**
     * 收藏
     */
    private Long collection;

    /**
     * 阅读计数
     */
    private Long read_count;

    /**
     * 摘要
     */
    private String summary;
}

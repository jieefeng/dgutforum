package com.dgutforum.article.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 文章信息
 * <p>
 * DTO 定义返回给web前端的实体类 (VO)
 */
@Data
public class ArticleDTO implements Serializable {
    private static final long serialVersionUID = -793906904770296838L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
    private Long deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdateTime;

    /**
     * 分类
     */
    private Long categoryId;

}

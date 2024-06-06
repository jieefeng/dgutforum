package com.dgutforum.comment.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@TableName("comment")
@Data
public class Comment {
    @TableId
    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private Long topCommentId;
    private Long parentCommentId;
    private short deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    @TableField(exist = false)  // 表示该字段在数据库表中不存在
    private List<Comment> children;
}

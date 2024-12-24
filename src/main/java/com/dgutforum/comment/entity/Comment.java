package com.dgutforum.comment.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@TableName("comment")
@Data
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private Long parise;
    private short deleted;
    private Long topCommentId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

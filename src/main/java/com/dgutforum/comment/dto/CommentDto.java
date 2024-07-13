package com.dgutforum.comment.dto;


import com.dgutforum.common.dto.BaseDO;
import lombok.Data;

@Data
public class CommentDto extends BaseDO {

    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private Long praise;
    private String username;
    private String phone;
}

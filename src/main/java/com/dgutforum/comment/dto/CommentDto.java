package com.dgutforum.comment.dto;


import com.dgutforum.common.dto.BaseDO;
import lombok.Data;

@Data
public class CommentDto extends BaseDO {

    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private Long topCommentId;
    private Long parentCommentId;
    private Long parise;
    private String userName;
    private String phone;
}

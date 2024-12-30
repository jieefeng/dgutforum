package com.dgutforum.comment.vo;

import com.dgutforum.common.dto.BaseDO;
import lombok.Data;

@Data
public class CommentChildVo extends BaseDO {
    private Long articleId;
    private String content;
    private Long userId;
    private Long praise;
    private String username;
    private String photo;
}

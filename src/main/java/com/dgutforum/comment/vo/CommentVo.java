package com.dgutforum.comment.vo;

import com.dgutforum.comment.dto.CommentDto;
import com.dgutforum.common.dto.BaseDO;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo extends BaseDO {

    private Long articleId;
    private String content;
    private Long userId;
    private Long praise;
    private String username;
    private String photo;
    private String topCommentId;
    private List<CommentChildVo> commentChildVoList;
}

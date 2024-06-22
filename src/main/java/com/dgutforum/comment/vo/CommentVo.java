package com.dgutforum.comment.vo;

import com.dgutforum.comment.dto.CommentDto;
import com.dgutforum.common.dto.BaseDO;
import lombok.Data;

import java.util.List;

@Data
public class CommentVo extends BaseDO {

    private Long id;
    private Long articleId;
    private String content;

    private Long topCommentId;
    private Long parentCommentId;

    private Long userId;
    private Long parise;
    private String userName;
    private String photo;

    private List<CommentDto> children;
}

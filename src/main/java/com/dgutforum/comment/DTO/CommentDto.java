package com.dgutforum.comment.DTO;

import com.dgutforum.comment.vo.CommentVo;
import lombok.Data;

import java.util.List;

@Data
public class CommentDto {

    private Long id;
    private Long articleId;
    private String content;

    private Long topCommentId;
    private Long parentCommentId;

    private Long userId;
    private Long parise;
    private String userName;
    private String phone;

    private List<CommentVo> children;
}

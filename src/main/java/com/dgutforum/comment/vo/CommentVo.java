package com.dgutforum.comment.vo;


import com.dgutforum.comment.entity.Comment;
import com.dgutforum.common.dto.BaseDO;
import lombok.Data;
import java.util.List;

@Data
public class CommentVo extends BaseDO {

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

package com.dgutforum.comment.req;

import lombok.Data;

/**
 * 评论列表入参
 */
@Data
public class CommentUpdateReq {

    /**
     * 评论ID
     */
    private Long commentId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 评论内容
     */
    private String commentContent;

}

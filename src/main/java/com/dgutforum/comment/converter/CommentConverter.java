package com.dgutforum.comment.converter;


import com.dgutforum.comment.entity.Comment;
import com.dgutforum.comment.req.CommentSaveReq;

import java.time.LocalDateTime;

/**
 * 文章转换
 * <p>
 */
public class CommentConverter {

    public static Comment toComment(CommentSaveReq req, Long userId) {
        Comment comment = new Comment();
        // 设置作者ID
        comment.setUserId(userId);
        comment.setArticleId(req.getArticleId());
        comment.setCreateTime(LocalDateTime.now());
        comment.setUpdateTime(LocalDateTime.now());
        comment.setDeleted((short) 0);
        comment.setContent(req.getCommentContent());
        return comment;
    }
}

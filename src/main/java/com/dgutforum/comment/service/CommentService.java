package com.dgutforum.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dgutforum.Common.result.ResVo;
import com.dgutforum.comment.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {

    ResVo<List<Comment>> getCommentsByArticleId(Long articleId);

}

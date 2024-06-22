package com.dgutforum.comment.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dgutforum.comment.req.CommentListReq;
import com.dgutforum.comment.vo.CommentVo;
import com.dgutforum.common.result.ResVo;
import com.dgutforum.comment.entity.Comment;

import java.util.List;

public interface CommentService extends IService<Comment> {

//    ResVo<List<Comment>> getCommentsByArticleId(Long articleId);

    ResVo<List<CommentVo>> list(CommentListReq commentListReq);

}

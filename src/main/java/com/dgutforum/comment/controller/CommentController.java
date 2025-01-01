package com.dgutforum.comment.controller;

import cn.hutool.core.bean.BeanUtil;
import com.dgutforum.comment.dto.CommentDto;
import com.dgutforum.comment.req.CommentListReq;
import com.dgutforum.comment.req.CommentReq;
import com.dgutforum.comment.vo.CommentVo;
import com.dgutforum.common.result.ResVo;
import com.dgutforum.comment.converter.CommentConverter;
import com.dgutforum.comment.entity.Comment;
import com.dgutforum.comment.service.CommentService;
import com.dgutforum.comment.req.CommentSaveReq;

import com.dgutforum.common.result.Result;
import com.dgutforum.context.ThreadLocalContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.dgutforum.common.result.eunms.StatusEnum.COMMENT_NOT_EXISTS;
import static net.sf.jsqlparser.parser.feature.Feature.comment;

@RestController
@RequestMapping(path = "comment")
@Tag(name = "评论相关接口")
public class CommentController {

    @Resource
    private CommentService commentService;


    /**
     * 保存评论
     * @param req
     * @return
     */
    @PostMapping(path = "save")
    @Operation(summary = "保存评论")
    public Result save(@RequestBody CommentSaveReq req) {
        Comment comment = CommentConverter.toComment(req, ThreadLocalContext.getUserId());
        if(commentService.save(comment)){
            return Result.success();
        } else {
            return Result.error("评论不存在");
        }
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除评论")
    public Result deleteComment(@PathVariable Long id) {
        return Result.success(commentService.removeById(id));
    }

    /**
     * 更新评论
     * @param commentReq
     * @return
     */
    @PutMapping
    @Operation(summary = "更新评论")
    public Result updateComment(@RequestBody CommentReq commentReq) {

        Comment comment = new Comment();
        BeanUtil.copyProperties(comment,commentReq);
        comment.setUpdateTime(LocalDateTime.now());
        return Result.success(commentService.updateById(comment));
    }

    /**
     * 查询单条评论
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "查询单条评论")
    public Result getComment(@PathVariable Long id) {
        return Result.success(commentService.getById(id));
    }

//    @GetMapping("/article/{articleId}")
//    public ResVo<List<Comment>> getCommentsByArticleId(@PathVariable Long articleId) {
//        return commentService.getCommentsByArticleId(articleId);
//    }

    /**
     * 根据文章id查询评价
     * @param articleId
     * @return
     */
    @GetMapping("/list/{articleId}")
    @Operation(summary = "根据文章id查询评价")
    public Result list(@PathVariable Long articleId){
      return Result.success(commentService.list(articleId));
    }
}

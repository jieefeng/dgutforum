package com.dgutforum.comment.controller;

import com.dgutforum.comment.DTO.CommentDto;
import com.dgutforum.comment.req.CommentListReq;
import com.dgutforum.comment.vo.CommentVo;
import com.dgutforum.common.result.ResVo;
import com.dgutforum.comment.converter.CommentConverter;
import com.dgutforum.comment.entity.Comment;
import com.dgutforum.comment.service.CommentService;
import com.dgutforum.comment.req.CommentSaveReq;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.dgutforum.common.result.eunms.StatusEnum.COMMENT_NOT_EXISTS;

@RestController
@RequestMapping(path = "comment")
public class CommentController {

    @Resource
    private CommentService commentService;


    /**
     * 保存评论
     * @param req
     * @return
     */
    @PostMapping(path = "post")
    @ResponseBody
    public ResVo<Long> save(@RequestBody CommentSaveReq req) {
        Comment comment = CommentConverter.toComment(req, 1L);
        if(commentService.save(comment)){
            return ResVo.ok(comment.getId());
        } else {
            return ResVo.fail(COMMENT_NOT_EXISTS);
        }
    }

    /**
     * 删除评论
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResVo<Boolean> deleteComment(@PathVariable Long id) {
        return ResVo.ok(commentService.removeById(id));
    }

    /**
     * 更新评论
     * @param comment
     * @return
     */
    @PutMapping
    public ResVo<Boolean> updateComment(@RequestBody Comment comment) {
        return ResVo.ok(commentService.updateById(comment));
    }

    /**
     * 查询单条评论
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResVo<Comment> getComment(@PathVariable Long id) {
        return ResVo.ok(commentService.getById(id));
    }

//    @GetMapping("/article/{articleId}")
//    public ResVo<List<Comment>> getCommentsByArticleId(@PathVariable Long articleId) {
//        return commentService.getCommentsByArticleId(articleId);
//    }

    /**
     * 根据文章id查询评价
     * @param commentListReq
     * @return
     */
    @PostMapping(path = "list")
    public ResVo<List<CommentDto>> list(CommentListReq commentListReq){
      return commentService.list(commentListReq);
    }
}

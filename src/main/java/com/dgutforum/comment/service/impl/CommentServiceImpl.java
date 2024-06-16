package com.dgutforum.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgutforum.comment.DTO.CommentDto;
import com.dgutforum.mapper.CommentUserInfoMapper;
import com.dgutforum.comment.req.CommentListReq;
import com.dgutforum.common.result.ResVo;
import com.dgutforum.comment.entity.Comment;
import com.dgutforum.mapper.CommentMapper;
import com.dgutforum.comment.service.CommentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private CommentUserInfoMapper commentUserInfoMapper;


    @Override
    public ResVo<List<CommentDto>> list(CommentListReq commentListReq) {

        //1.根据文章id查询评价 每次查询十个
        List<CommentDto> list = listTen(commentListReq);
        //2.根据文章id和父评论id查询所有子评论   并且根据更新时间排序
        for(CommentDto commentDto : list){
            commentDto.setChildren(commentUserInfoMapper.queryChildrenComment(commentDto.getArticleId(),commentDto.getId()));
        }
        return ResVo.ok(list);
    }

    /**
     * 根据文章id分页查询评价
     * @param commentListReq
     * @return
     */
    public List<CommentDto> listTen(CommentListReq commentListReq){
        PageHelper.startPage(commentListReq.getPageNumber(),commentListReq.getPageSsize());
        Page<CommentDto> commentDtoPage = commentUserInfoMapper.list(commentListReq.getArticleId());
        long total = commentDtoPage.getTotal();
        List<CommentDto> result = commentDtoPage.getResult();
        return result;
    }

//    // TODO 尚未实现评论分页查询 目前是查询出文章的所有评论
//    public ResVo<List<Comment>> getCommentsByArticleId(Long articleId) {
//        // 获取某文章的所有未删除的评论，按创建时间排序
//        List<Comment> comments = baseMapper.selectList(
//                new QueryWrapper<Comment>().eq("article_id", articleId).eq("deleted", 0).orderByAsc("create_time"));
//
//        // 使用Map以id为键来组织评论
//        Map<Long, Comment> commentMap = comments.stream().collect(Collectors.toMap(Comment::getId, comment -> comment));
//
//        // 初始化顶级评论列表
//        List<Comment> rootComments = new ArrayList<>();
//
//        // 构建评论树
//        for (Comment comment : comments) {
//            if (comment.getTopCommentId() == 0) {
//                rootComments.add(comment);
//            } else {
//                Comment parent = commentMap.get(comment.getParentCommentId());
//                if (parent != null) {
//                    if (parent.getChildren() == null) {
//                        parent.setChildren(new ArrayList<>());
//                    }
//                    parent.getChildren().add(comment);
//                }
//            }
//        }
//        return ResVo.ok(rootComments);
//    }
}

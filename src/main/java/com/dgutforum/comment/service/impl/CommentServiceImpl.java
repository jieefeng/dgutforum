package com.dgutforum.comment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgutforum.Common.result.ResVo;
import com.dgutforum.comment.entity.Comment;
import com.dgutforum.comment.mapper.CommentMapper;
import com.dgutforum.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    // TODO 尚未实现评论分页查询 目前是查询出文章的所有评论
    public ResVo<List<Comment>> getCommentsByArticleId(Long articleId) {
        // 获取某文章的所有未删除的评论，按创建时间排序
        List<Comment> comments = baseMapper.selectList(
                new QueryWrapper<Comment>().eq("article_id", articleId).eq("deleted", 0).orderByAsc("create_time"));

        // 使用Map以id为键来组织评论
        Map<Long, Comment> commentMap = comments.stream().collect(Collectors.toMap(Comment::getId, comment -> comment));

        // 初始化顶级评论列表
        List<Comment> rootComments = new ArrayList<>();

        // 构建评论树
        for (Comment comment : comments) {
            if (comment.getTopCommentId() == 0) {
                rootComments.add(comment);
            } else {
                Comment parent = commentMap.get(comment.getParentCommentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(comment);
                }
            }
        }
        return ResVo.ok(rootComments);
    }
}

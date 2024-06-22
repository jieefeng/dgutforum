package com.dgutforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgutforum.comment.dto.CommentDto;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public abstract interface CommentUserInfoMapper extends BaseMapper<com.dgutforum.comment.dto.CommentDto> {



    @Select("select c.id, c.article_id, c.user_id, c.content, c.top_comment_id, c.parent_comment_id, c.praise, c.create_time, c.update_time," +
            "ui.user_name,ui.photo " +
            "from comment c " +
            "left join dgutforum.user_info ui on c.user_id = ui.user_id " +
            "where c.article_id = #{articleId} and c.deleted = 0 " +
            "order by c.update_time")
    Page<com.dgutforum.comment.dto.CommentDto> list (Long articleId);

    @Select("select c.id, c.article_id, c.user_id, c.content, c.top_comment_id, c.parent_comment_id, c.praise, c.create_time, c.update_time," +
            "ui.user_name,ui.photo " +
            "from comment c " +
            "left join dgutforum.user_info ui on c.user_id = ui.user_id " +
            "where c.article_id = #{articleId} and c.deleted = 0 and c.parent_comment_id = #{parentCommentId} " +
            "order by c.update_time")
    List<CommentDto> queryChildrenComment(Long articleId, Long parentCommentId);


    /**
     * 根据文章id查询所有一级评论
     * @param articleId
     * @return
     */
    @Select("select c.id, c.article_id, c.user_id, c.content, c.top_comment_id, c.parent_comment_id, c.praise, c.create_time, c.update_time," +
            "ui.user_name,ui.photo " +
            "from comment c " +
            "left join dgutforum.user_info ui on c.user_id = ui.user_id " +
            "where c.article_id = #{articleId} and c.deleted = 0 and c.top_comment_id = c.id " +
            "order by c.update_time")
    List<com.dgutforum.comment.dto.CommentDto> queryCommentByArticleId(Long articleId);
}




























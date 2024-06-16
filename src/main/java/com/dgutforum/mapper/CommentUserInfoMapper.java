package com.dgutforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgutforum.comment.DTO.CommentDto;
import com.dgutforum.comment.vo.CommentVo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public abstract interface CommentUserInfoMapper extends BaseMapper<CommentDto> {



    @Select("select c.id, c.article_id, c.user_id, c.content, c.top_comment_id, c.parent_comment_id, c.praise, c.create_time, c.update_time," +
            "ui.user_name,ui.photo " +
            "from comment c " +
            "left join dgutforum.user_info ui on c.user_id = ui.user_id " +
            "where c.article_id = #{articleId} and c.deleted = 0 " +
            "order by c.update_time")
    Page<CommentDto> list (Long articleId);

    @Select("select c.id, c.article_id, c.user_id, c.content, c.top_comment_id, c.parent_comment_id, c.praise, c.create_time, c.update_time," +
            "ui.user_name,ui.photo " +
            "from comment c " +
            "left join dgutforum.user_info ui on c.user_id = ui.user_id " +
            "where c.article_id = #{articleId} and c.deleted = 0 and c.parent_comment_id = #{parentCommentId} " +
            "order by c.update_time")
    List<CommentVo> queryChildrenComment(Long articleId, Long parentCommentId);



}




























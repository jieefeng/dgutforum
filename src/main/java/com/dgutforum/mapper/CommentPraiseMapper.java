package com.dgutforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgutforum.activity.vo.ActivityVo;
import com.dgutforum.comment.entity.Comment;
import com.dgutforum.comment.entity.CommentPraise;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentPraiseMapper extends BaseMapper<CommentPraise> {

    @Insert("insert into comment_praise(user_id,comment_id) value (#{userId},#{commentId})")
    void save(ActivityVo activityVo);
    @Insert("select id from comment_praise where user_id = #{userId} and comment_id = {commentId}} ")
    Long isPraise(Long userId,Long commentId);

}

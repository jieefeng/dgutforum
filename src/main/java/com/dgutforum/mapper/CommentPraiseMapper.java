package com.dgutforum.mapper;

import com.dgutforum.activity.vo.ActivityVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentPraiseMapper {

    @Insert("insert into comment_praise(user_id,comment_id) value (#{userId},#{commentId})")
    void save(ActivityVo activityVo);

}

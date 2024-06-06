package com.dgutforum.comment.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgutforum.comment.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}

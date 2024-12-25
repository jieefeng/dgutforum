package com.dgutforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgutforum.article.entity.ReadHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReadHistoryMapper extends BaseMapper<ReadHistory> {

    @Insert("insert into read_history(user_id,article_id,read_time) value (#{userId},#{articleId},#{readTime})")
    void save(ReadHistory readHistory);

    @Select("select * from read_history where user_id = #{userId} and article_id = #{articleId}")
    void query(ReadHistory readHistory);
}

package com.dgutforum.mapper;

import com.dgutforum.article.entity.ReadHistory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ReadHistoryMapper {

    @Insert("insert into read_history(user_id,article_id) value (#{userId},#{articleId})")
    void save(ReadHistory readHistory);

    @Select("select * from read_history where user_id = #{userId} and article_id = #{articleId}")
    void query(ReadHistory readHistory);
}

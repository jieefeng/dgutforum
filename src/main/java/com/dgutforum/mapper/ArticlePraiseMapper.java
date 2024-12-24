package com.dgutforum.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;

@Mapper
public interface ArticlePraiseMapper {

    @Select("select article_id " +
            "from article_praise " +
            "where user_id = #{userId}")
    ArrayList<Long> getArticleIdByuserId(Long userId);

    @Insert("insert into article_praise (user_id, article_id) value (#{userId},#{articleId})")
    void save(Long articleId, Long userId);

    @Select("select * from article_praise " +
            "where article_id = #{articleId}")
    Long getArticlePraiseByArticleId(Long articleId);

}

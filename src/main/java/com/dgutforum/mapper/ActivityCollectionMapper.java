package com.dgutforum.mapper;

import com.dgutforum.article.vo.ArticleUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ActivityCollectionMapper {

    @Select("select article_id from article_collection " +
            "where user_id = #{userId}")
    List<Long> getArticleUserCollectionByUserId(Long userId);
}

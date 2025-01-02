package com.dgutforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgutforum.article.entity.ArticleCollection;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleCollectionMapper extends BaseMapper<ArticleCollection> {

    @Select("select article_id from article_collection " +
            "where user_id = #{userId}")
    List<Long> getArticleUserCollectionByUserId(Long userId);

    @Delete("delete from article_collection where article_id = #{articleId} and user_id = #{userId}")
    void deleteCollection(ArticleCollection articleCollection);
}

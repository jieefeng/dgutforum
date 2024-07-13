package com.dgutforum.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgutforum.article.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;


@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Update("update article " +
            "set praise = praise + 1 " +
            "where id = #{articleId}")
    void praise(Long articleId);
}

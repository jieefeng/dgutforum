package com.dgutforum.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgutforum.article.entity.Article;
import com.dgutforum.article.vo.ArticleUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

import java.util.List;


@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    @Update("update article " +
            "set praise = praise + 1 " +
            "where id = #{articleId}")
    void praise(Long articleId);

    @Update("update article " +
            "set praise = praise - 1 " +
            "where id = #{articleId}")
    void cancelPraise(Long articleId);
}

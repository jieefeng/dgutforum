package com.dgutforum.mapper;

import com.dgutforum.article.vo.ArticleUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleUserMapper {

    @Select("select a.*,ui.username,ui.photo " +
            "from article a left join user ui  on a.user_id = ui.id " +
            "where a.id = #{id} and ui.id = #{userId} " +
            "order by a.update_time desc ")
    ArticleUserVo queryArticleUserInfo(Long id, Long userId);

    @Select("select a.*,ui.username,ui.photo " +
            "from article a left join user ui on a.user_id = ui.id " +
            "where a.category_id = #{categoryId} " +
            "order by a.update_time desc ")
    List<ArticleUserVo> queryArticleUserInfoByCategoryId(Long categoryId);

    @Select("select a.*,ui.username,ui.photo " +
            "from article a left join user ui on a.user_id = ui.id " +
            "order by a.update_time desc ")
    List<ArticleUserVo> queryArticleUserInfoAll();
}

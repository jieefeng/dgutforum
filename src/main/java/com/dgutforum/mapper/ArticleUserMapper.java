package com.dgutforum.mapper;

import com.dgutforum.article.vo.ArticleUserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleUserMapper {

    @Select("select a.*,ui.username,ui.photo " +
            "from article a left join user ui  on a.user_id = ui.id " +
            "where a.id = #{id} and ui.id = #{userId} and a.deleted = 0 " +
            "order by a.update_time desc ")
    ArticleUserVo queryArticleUserInfo(Long id, Long userId);

    @Select("select a.*,ui.username,ui.photo " +
            "from article a left join user ui on a.user_id = ui.id " +
            "where a.category_id = #{categoryId} and a.deleted = 0  " +
            "order by a.update_time desc ")
    List<ArticleUserVo> queryArticleUserInfoByCategoryId(Long categoryId);

    @Select("select a.*,ui.username,ui.photo " +
            "from article a left join user ui on a.user_id = ui.id " +
            "where a.deleted = 0 " +
            "order by a.update_time desc ")
    List<ArticleUserVo> queryArticleUserInfoAll();

    @Select("select a.*,ui.username,ui.photo " +
            "from article a left join user ui on a.user_id = ui.id " +
            "where a.id = #{articleId} and a.deleted = 0 ")
    ArticleUserVo queryOneArticleUserInfoByarticleId(Long articleId);

    @Select("select a.*,ui.username,ui.photo " +
            "from article a left join user ui on a.user_id = ui.id " +
            "where a.user_id = #{userId} and a.deleted = 0 ")
    List<ArticleUserVo> getArticleUserByArticleId(Long userId);
}
































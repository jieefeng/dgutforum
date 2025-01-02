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

    @Select("select a.*,ui.username,ui.photo,cy.category_name " +
            "from article a left join user ui on a.user_id = ui.id " +
            "left join category cy on a.category_id = cy.id " +
            "where a.user_id = #{userId} and a.deleted = 0 ")
    List<ArticleUserVo> getArticleUserByArticleId(Long userId);

    @Select("select a.*,ui.username,ui.photo " +
            "from article a left join user ui on a.user_id = ui.id " +
            "where a.deleted = 0 ")
    List<ArticleUserVo> queryAll();

    @Select("select a.*,ui.username,ui.photo,cy.category_name " +
            "from category cy, read_history his left join article a on his.article_id = a.id " +
            "left join user ui on a.user_id = ui.id " +
            "where a.category_id = cy.id and his.user_id = #{userId} and a.deleted = 0 ")
    List<ArticleUserVo> getReadHistoryByUserId(long id);

    @Select("select a.*,ui.username,ui.photo,cy.category_name " +
            "from category cy, article_collection acol left join article a on acol.article_id = a.id " +
            "left join user ui on a.user_id = ui.id " +
            "where a.category_id = cy.id and acol.user_id = #{userId} and a.deleted = 0 ")
    List<ArticleUserVo> getCollectionByUserId(long id);

    @Select("select a.*, ui.username, ui.photo, cy.category_name " +
            "from article a left join user ui on a.user_id = ui.id " +
            "left join category cy on a.category_id = cy.id " +
            "where a.title like concat('%', #{key}, '%')")
    List<ArticleUserVo> searchByKey(String key);
}
































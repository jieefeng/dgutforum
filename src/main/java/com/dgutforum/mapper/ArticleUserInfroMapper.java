package com.dgutforum.mapper;

import com.dgutforum.article.Do.ArticleUserDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ArticleUserInfroMapper {

    @Select("select a.*,ui.user_name,ui.photo " +
            "from article a left join user_info ui  on a.user_id = ui.id " +
            "where a.id = #{id} and ui.id = #{userId} " +
            "order by a.update_time desc ")
    ArticleUserDo queryArticleUserInfo(Long id,Long userId);

    @Select("select a.*,ui.user_name,ui.photo " +
            "from article a left join user_info ui on a.user_id = ui.id " +
            "where a.category_id = #{categoryId} " +
            "order by a.update_time desc ")
    List<ArticleUserDo> queryArticleUserInfoByCategoryId(Long categoryId);
}

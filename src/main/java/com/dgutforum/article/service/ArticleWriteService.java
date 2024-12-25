package com.dgutforum.article.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dgutforum.article.req.praiseVo;
import com.dgutforum.article.vo.ArticleUserVo;
import com.dgutforum.article.entity.Article;
import com.dgutforum.article.req.ArticlePostReq;
import com.dgutforum.article.vo.BrowseHistoryVo;

import java.util.List;

public interface ArticleWriteService extends IService<Article> {

    /**
     * 保存or更新文章
     *
     * @param req    上传的文章体
     * @param author 作者
     * @return 返回文章主键
     */
    ArticleUserVo saveArticle(ArticlePostReq req, Long author);

    /**
     * 删除文章
     *
     * @param articleId   文章id
     * @param loginUserId 执行操作的用户
     */
    void deleteArticle(Long articleId, Long loginUserId);

    /**
     * 根据分类id查询文章
     * @param categoryId
     * @return
     */
    List<ArticleUserVo> getByCategoryId(Long categoryId);

    /**
     * 根据文章id查询文章
     * @param articleId
     * @return
     */
    ArticleUserVo getArticleUserVoById(Long articleId);

    /**
     * 根据用户id查询文章
     * @param praiseVo
     * @return
     */
    List<ArticleUserVo> getArticleUserByArticleId(praiseVo praiseVo);

    /**
     * 根据用户id查询点赞列表
     * @param praiseVo
     * @return
     */
    List<ArticleUserVo> getArticleUserPraiseByUserId(praiseVo praiseVo);

    /**
     * 用户点赞
     * @param praiseVo
     */
    void praise(praiseVo praiseVo);

    /**
     * 查询全部文章
     * @return
     */
    List<ArticleUserVo> selectAll();

    /**
     * 查询用户收藏的文章
     * @return
     */
    List<ArticleUserVo> getArticleUserCollectionByUserId();

    List<ArticleUserVo> getBorwseHistoryWithTime(BrowseHistoryVo browseHistoryVo);


}

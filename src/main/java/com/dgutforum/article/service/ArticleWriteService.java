package com.dgutforum.article.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dgutforum.article.req.ArticleGetReq;
import com.dgutforum.article.vo.ArticleUserVo;
import com.dgutforum.article.entity.Article;
import com.dgutforum.article.req.ArticlePostReq;

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
     * @param articleGetReq
     * @return
     */
    List<ArticleUserVo> getArticleUserByArticleId(ArticleGetReq articleGetReq);

    /**
     * 根据用户id查询点赞列表
     * @param articleGetReq
     * @return
     */
    List<ArticleUserVo> getArticleUserPraiseByUserId(ArticleGetReq articleGetReq);

    /**
     * 用户点赞
     * @param articleGetReq
     */
    void praise(ArticleGetReq articleGetReq);
}

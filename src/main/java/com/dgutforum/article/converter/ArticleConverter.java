package com.dgutforum.article.converter;


import com.dgutforum.article.entity.ArticleDO;
import com.dgutforum.article.vo.ArticlePostReq;

/**
 * 文章转换
 * <p>
 *
 * @author louzai
 * @date 2022-07-31
 */
public class ArticleConverter {

    public static ArticleDO toArticleDo(ArticlePostReq req, Long author) {
        ArticleDO article = new ArticleDO();
        // 设置作者ID
        article.setUserId(author);
        article.setId(req.getArticleId());
        article.setTitle(req.getTitle());
        article.setPicture(req.getCover() == null ? "" : req.getCover());
        article.setCategoryId(req.getCategoryId());
        article.setDeleted(0);
        article.setContext(req.getContent());
        return article;
    }
}

package com.dgutforum.article.converter;


import com.dgutforum.article.Do.ArticleDO;
import com.dgutforum.article.entity.ArticleDTO;
import com.dgutforum.article.vo.ArticlePostReq;

/**
 * 文章转换
 * <p>
 */
public class ArticleConverter {

    public static ArticleDTO toArticleDo(ArticlePostReq req, Long author) {
        ArticleDTO article = new ArticleDTO();
        // 设置作者ID
        article.setUserId(author);
        article.setId(req.getArticleId());
        article.setTitle(req.getTitle());
        article.setPicture(req.getCover() == null ? "" : req.getCover());
        article.setCategoryId(req.getCategoryId());
        article.setDeleted(0L);
        article.setContent(req.getContent());
        return article;
    }
}

package com.dgutforum.article.converter;



import com.dgutforum.article.dto.ArticleUserDto;
import com.dgutforum.article.entity.Article;
import com.dgutforum.article.vo.ArticlePostReq;

/**
 * 文章转换
 * <p>
 */
public class ArticleConverter {

    public static Article toArticle(ArticlePostReq req, Long author) {
        Article article = new Article();
        // 设置作者ID
        article.setUserId(author);
        article.setId(req.getArticleId());
        article.setTitle(req.getTitle());
        article.setPicture(req.getCover() == null ? "" : req.getCover());
        article.setCategoryId(req.getCategoryId());
        article.setDeleted((short) 0);
        article.setContent(req.getContent());
        return article;
    }
}

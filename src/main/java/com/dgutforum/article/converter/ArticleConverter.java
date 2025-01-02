package com.dgutforum.article.converter;



import com.dgutforum.article.entity.Article;

/**
 * 文章转换
 * <p>
 */
public class ArticleConverter {

    public static Article toArticle(Article req, Long author) {
        Article article = new Article();
        // 设置作者ID
        article.setUserId(author);
        article.setId(req.getId());
        article.setTitle(req.getTitle());
        article.setPicture(req.getPicture() == null ? "" : req.getPicture());
        article.setCategoryId(req.getCategoryId());
        article.setDeleted((short) 0);
        article.setContent(req.getContent());
        article.setSummary(req.getSummary());
        return article;
    }
}

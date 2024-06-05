package com.dgutforum.article.service.impl;

import com.dgutforum.Common.util.NumUtil;
import com.dgutforum.article.converter.ArticleConverter;
import com.dgutforum.article.entity.ArticleDO;
import com.dgutforum.article.service.ArticleWriteService;
import com.dgutforum.article.vo.ArticlePostReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

@Slf4j
@Service
public class ArticleWriteServiceImpl implements ArticleWriteService {

    @Resource
    private TransactionTemplate transactionTemplate;

    @Override
    public Long saveArticle(ArticlePostReq req, Long author) {
        ArticleDO article = ArticleConverter.toArticleDo(req, author);
        return transactionTemplate.execute(new TransactionCallback<Long>() {
            @Override
            public Long doInTransaction(TransactionStatus status) {
                Long articleId;
                if (NumUtil.nullOrZero(req.getArticleId())) {
                    articleId = insertArticle(article);
                    log.info("文章发布成功! title={}", req.getTitle());
                } else {
                    articleId = updateArticle(article);
                    log.info("文章更新成功！ title={}", article.getTitle());
                }
                return articleId;
            }
        });
    }





    @Override
    public void deleteArticle(Long articleId, Long loginUserId) {

    }
}

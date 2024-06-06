package com.dgutforum.article.service.impl;


import com.dgutforum.Common.util.NumUtil;
import com.dgutforum.article.converter.ArticleConverter;

import com.dgutforum.article.entity.Article;
import com.dgutforum.article.mapper.ArticleMapper;
import com.dgutforum.article.service.ArticleWriteService;
import com.dgutforum.article.vo.ArticlePostReq;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;

@Slf4j
@Service
public class ArticleWriteServiceImpl implements ArticleWriteService {

    @Resource
    private TransactionTemplate transactionTemplate;

    @Resource
    private ArticleMapper articleMapper;

    /**
     * 保存或更新文章
     * @param req    上传的文章体
     * @param author 作者
     * @return
     */
    @Override
    public Long saveArticle(ArticlePostReq req, Long author) {
        Article article = ArticleConverter.toArticle(req, author);
        return transactionTemplate.execute(new TransactionCallback<Long>() {
            @Override
            public Long doInTransaction(TransactionStatus status) {
                Long articleId;
                if (NumUtil.nullOrZero(req.getArticleId())) {
                    article.setCreateTime(LocalDateTime.now());
                    article.setLastUpdateTime(LocalDateTime.now());
                    articleId = insertArticle(article);
                    log.info("文章发布成功! title={}", req.getTitle());
                } else {
                    article.setCreateTime(LocalDateTime.now());
                    article.setLastUpdateTime(LocalDateTime.now());
                    articleId = updateArticle(article);
                    log.info("文章更新成功！ title={}", article.getTitle());
                }
                return articleId;
            }
        });
    }

    public Long updateArticle(Article article) {
        // 调用服务方法更新文章
        articleMapper.updateById(article);
        return article.getId();
    }

    private Long insertArticle(Article article) {
        // 插入文章
        articleMapper.insert(article);
        // 返回生成的主键 ID
        return article.getId();
    }


    @Override
    public void deleteArticle(Long articleId, Long loginUserId) {

    }
}

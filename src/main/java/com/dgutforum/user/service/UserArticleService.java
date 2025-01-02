package com.dgutforum.user.service;

import com.dgutforum.article.vo.ArticleBasicInfoVo;
import com.dgutforum.article.vo.ArticleUserVo;

import java.util.List;

public interface UserArticleService {
    List<ArticleUserVo> getArticleUserByArticleId(long id);

    List<ArticleUserVo> getReadHistoryByUserId(long id);

    List<ArticleUserVo> getCollectionByUserId(long id);
}

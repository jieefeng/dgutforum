package com.dgutforum.article.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dgutforum.article.Do.ArticleUserDo;
import com.dgutforum.article.dto.ArticleUserDto;
import com.dgutforum.article.entity.Article;
import com.dgutforum.article.vo.ArticlePostReq;

import java.util.List;

public interface ArticleWriteService extends IService<Article> {

    /**
     * 保存or更新文章
     *
     * @param req    上传的文章体
     * @param author 作者
     * @return 返回文章主键
     */
    ArticleUserDo saveArticle(ArticlePostReq req, Long author);

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
    List<ArticleUserDo> getByCategoryId(Long categoryId);
}

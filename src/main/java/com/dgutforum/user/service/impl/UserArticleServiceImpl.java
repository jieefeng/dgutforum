package com.dgutforum.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.dgutforum.article.vo.ArticleBasicInfoVo;
import com.dgutforum.article.vo.ArticleUserVo;
import com.dgutforum.comment.entity.Comment;
import com.dgutforum.mapper.ArticleUserMapper;
import com.dgutforum.mapper.CommentMapper;
import com.dgutforum.user.service.UserArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserArticleServiceImpl implements UserArticleService {

    @Autowired
    private ArticleUserMapper articleUserMapper;

    @Autowired
    private CommentMapper commentMapper;

    public Long countCommentsByArticleId(Long id) {
        QueryWrapper<Comment> wrapper = new QueryWrapper<>();
        wrapper.eq("article_id", id);
        return commentMapper.selectCount(wrapper);
    }

    @Override
    public List<ArticleUserVo> getArticleUserByArticleId(long id) {
        List<ArticleUserVo> articleUserVoList = articleUserMapper.getArticleUserByArticleId(id);

        for (ArticleUserVo articleUserVo : articleUserVoList) {
            articleUserVo.setCommentNumber(countCommentsByArticleId(articleUserVo.getId()));
        }

        return articleUserVoList;
    }

    @Override
    public List<ArticleUserVo> getReadHistoryByUserId(long id) {
        List<ArticleUserVo> articleUserVoList = articleUserMapper.getReadHistoryByUserId(id);

        for (ArticleUserVo articleUserVo : articleUserVoList) {
            articleUserVo.setCommentNumber(countCommentsByArticleId(articleUserVo.getId()));
        }

        return articleUserVoList;
    }

    @Override
    public List<ArticleUserVo> getCollectionByUserId(long id) {
        List<ArticleUserVo> articleUserVoList = articleUserMapper.getCollectionByUserId(id);

        for (ArticleUserVo articleUserVo : articleUserVoList) {
            articleUserVo.setCommentNumber(countCommentsByArticleId(articleUserVo.getId()));
        }

        return articleUserVoList;
    }
}

package com.dgutforum.article.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgutforum.article.req.ArticleGetReq;
import com.dgutforum.article.vo.ArticleUserVo;
import com.dgutforum.common.util.NumUtil;
import com.dgutforum.article.converter.ArticleConverter;

import com.dgutforum.article.entity.Article;
import com.dgutforum.image.service.ImageService;
import com.dgutforum.mapper.ArticleMapper;
import com.dgutforum.article.service.ArticleWriteService;
import com.dgutforum.article.req.ArticlePostReq;
import com.dgutforum.mapper.ArticlePraiseMapper;
import com.dgutforum.mapper.ArticleUserMapper;
import com.dgutforum.mapper.CommentMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ArticleWriteServiceImpl extends ServiceImpl<ArticleMapper,Article> implements ArticleWriteService {

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private ArticleMapper articleMapper;

    @Resource
    private ImageService imageService;

    @Resource
    private ArticleUserMapper articleUserMapper;

    @Resource
    private CommentMapper commentMapper;

    @Resource
    private ArticlePraiseMapper articlePraiseMapper;

    /**
     * 根据分类id查询文章
     * @param categoryId
     * @return
     */
   public List<ArticleUserVo>  getByCategoryId(Long categoryId) {
       log.info("根据分类id查询文章:{}",categoryId);
       List<ArticleUserVo> articleUserVos = null;
       //1.如果分类id为12  全表查询
       if(categoryId == 12){
           articleUserVos = articleUserMapper.queryArticleUserInfoAll();
       } else {
            articleUserVos = articleUserMapper.queryArticleUserInfoByCategoryId(categoryId);
       }
       //2.查询评论数
       for (ArticleUserVo articleUserVo : articleUserVos){
           QueryWrapper queryWrapper = new QueryWrapper<>();
           queryWrapper.eq("article_id", articleUserVo.getId());
           articleUserVo.setCommentNumber(commentMapper.selectCount(queryWrapper));
       }
       return articleUserVos;
   }

    /**
     * 根据文章id查询文章
     * @param articleId
     * @return
     */
    @Override
    public ArticleUserVo getArticleUserVoById(Long articleId) {
        log.info("根据文章id查询文章:{}",articleId);
        //1.根据文章id查询文章
        ArticleUserVo articleUserVo = articleUserMapper.queryOneArticleUserInfoByarticleId(articleId);
        //2.查询文章评论数
        QueryWrapper queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleUserVo.getId());
        articleUserVo.setCommentNumber(commentMapper.selectCount(queryWrapper));
        return articleUserVo;
    }

    /**
     * 根据用户id查询文章
     * @param articleGetReq
     * @return
     */
    @Override
    public List<ArticleUserVo> getArticleUserByArticleId(ArticleGetReq articleGetReq) {
        List<ArticleUserVo> articleUserByArticleId = articleUserMapper.getArticleUserByArticleId(articleGetReq.getUserId());
        log.info("根据用户id查询文章:{}",articleUserByArticleId);
        return articleUserByArticleId;
    }

    /**
     * 根据用户id查询点赞列表
     * @param articleGetReq
     * @return
     */
    @Override
    public List<ArticleUserVo> getArticleUserPraiseByUserId(ArticleGetReq articleGetReq) {
        //1.根据用户id查询用户点赞过的文章id列表
        ArrayList<Long> praiseList = articlePraiseMapper.getArticleIdByuserId(articleGetReq.getUserId());
        //2.查询文章列表
        List<ArticleUserVo> articleUserVos = new ArrayList<>(praiseList.size());
        for (Long articleId : praiseList){
            articleUserVos.add(articleUserMapper.queryOneArticleUserInfoByarticleId(articleId));
        }
        return articleUserVos;
    }

    /**
     * 用户点赞
     * @param articleGetReq
     */
    @Override
    public void praise(ArticleGetReq articleGetReq) {
        //1.文章表点赞数+1
        articleMapper.praise(articleGetReq.getArticleId());
        //2.article_praise增加关系
        articlePraiseMapper.save(articleGetReq.getArticleId(),articleGetReq.getUserId());
    }

    /**
     * 保存或更新文章
     * @param req    上传的文章体
     * @param author 作者
     * @return
     */
    @Override
    public ArticleUserVo saveArticle(ArticlePostReq req, Long author) {
        //请请求体转为数据库实体
        Article article = ArticleConverter.toArticle(req, author);
        //将图片转为url链接
        if(article != null)
            article.setPicture(imageService.saveImage(article.getPicture()));

        return transactionTemplate.execute(new TransactionCallback<ArticleUserVo>() {
            @Override
            public ArticleUserVo doInTransaction(TransactionStatus status) {
                Long articleId;
                if (NumUtil.nullOrZero(req.getArticleId())) {
                    article.setCreateTime(LocalDateTime.now());
                    article.setUpdateTime(LocalDateTime.now());
                    articleId = insertArticle(article);
                    log.info("文章发布成功! title={}", req.getTitle());
                } else {
                    article.setCreateTime(LocalDateTime.now());
                    article.setUpdateTime(LocalDateTime.now());
                    articleId = updateArticle(article);
                    log.info("文章更新成功！ title={}", article.getTitle());
                }
                ArticleUserVo articleUserVo = queryArticleUserInfo(article.getId(),article.getUserId());
                return articleUserVo;
            }
        });
    }

    /**
     * 查询文章信息和用户信息
     * @param id
     * @param userId
     * @return
     */
    private ArticleUserVo queryArticleUserInfo(Long id, Long userId) {
        ArticleUserVo articleUserVo = articleUserMapper.queryArticleUserInfo(id,userId);
        return articleUserVo;
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

package com.dgutforum.article.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgutforum.activity.eums.StatusEnums;
import com.dgutforum.activity.service.impl.ActivityServiceImpl;
import com.dgutforum.activity.vo.ActivityVo;
import com.dgutforum.article.entity.ArticleCollection;
import com.dgutforum.article.entity.ArticlePraise;
import com.dgutforum.article.entity.ReadHistory;
import com.dgutforum.article.vo.PraiseVo;
import com.dgutforum.article.vo.ArticleUserVo;
import com.dgutforum.article.vo.BrowseHistoryVo;
import com.dgutforum.comment.entity.Comment;
import com.dgutforum.comment.entity.CommentPraise;
import com.dgutforum.common.util.NumUtil;
import com.dgutforum.article.converter.ArticleConverter;
import com.dgutforum.article.entity.Article;
import com.dgutforum.config.RabbitmqConfig;
import com.dgutforum.context.ThreadLocalContext;
import com.dgutforum.image.service.ImageService;
import com.dgutforum.mapper.*;
import com.dgutforum.article.service.ArticleWriteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class ArticleWriteServiceImpl extends ServiceImpl<ArticleMapper,Article> implements ArticleWriteService {

    private final TransactionTemplate transactionTemplate;
    private final ArticleMapper articleMapper;
    private final ImageService imageService;
    private final ArticleUserMapper articleUserMapper;
    private final CommentMapper commentMapper;
    private final ArticlePraiseMapper articlePraiseMapper;
    private final UserInfoMapper userInfoMapper;
    private final ArticleCollectionMapper articleCollectionMapper;
    private final ReadHistoryMapper readHistoryMapper;
    private final RabbitTemplate rabbitTemplate;
    private final ActivityServiceImpl activityServiceImpl;
    private final CommentPraiseMapper commentPraiseMapper;


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
        Long commentNumber = commentMapper.selectCount(queryWrapper);
        articleUserVo.setCommentNumber(commentNumber != null ? commentNumber : 0);
//        //3.查询文章点赞数
//        List<ArticlePraise> praiseNumber = articlePraiseMapper.getArticlePraiseByArticleId(articleId);
//        articleUserVo.setPraise((long) (praiseNumber != null ? praiseNumber.size() : 0));
        return articleUserVo;
    }

    /**
     * 根据用户id查询文章
     * @param praiseVo
     * @return
     */
    @Override
    public List<ArticleUserVo> getArticleUserByArticleId(PraiseVo praiseVo) {
        List<ArticleUserVo> articleUserByArticleId = articleUserMapper.getArticleUserByArticleId(praiseVo.getUserId());
        log.info("根据用户id查询文章:{}",articleUserByArticleId);
        return articleUserByArticleId;
    }

    /**
     * 根据用户id查询点赞列表
     * @param praiseVo
     * @return
     */
    @Override
    public List<ArticleUserVo> getArticleUserPraiseByUserId(PraiseVo praiseVo) {
        //1.根据用户id查询用户点赞过的文章id列表
        ArrayList<Long> praiseList = articlePraiseMapper.getArticleIdByuserId(praiseVo.getUserId());
        //2.查询文章列表
        List<ArticleUserVo> articleUserVos = new ArrayList<>(praiseList.size());
        for (Long articleId : praiseList){
            articleUserVos.add(articleUserMapper.queryOneArticleUserInfoByarticleId(articleId));
        }
        return articleUserVos;
    }

    /**
     * 用户点赞
     * @param praiseVo
     */
    @Override
    public void praise(PraiseVo praiseVo) {
        //1.article_praise增加关系
        Long articleId = praiseVo.getArticleId();
        Long userId = ThreadLocalContext.getUserId();
        //1.1如果已经有数据 则该点赞无效
        LambdaQueryWrapper<ArticlePraise> articlePraiseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articlePraiseLambdaQueryWrapper.eq(ArticlePraise::getArticleId,articleId);
        articlePraiseLambdaQueryWrapper.eq(ArticlePraise::getUserId, userId);
        if(articlePraiseMapper.selectOne(articlePraiseLambdaQueryWrapper) != null){
            //有数据
            return;
        }
        articlePraiseMapper.save(articleId, userId);
        //2.文章表点赞数+1
        articleMapper.praise(articleId);
        //3.文章作者的被点赞数加1
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getId,articleId);
        Article article = articleMapper.selectOne(articleLambdaQueryWrapper);
        Long authorId = article.getUserId();
        userInfoMapper.incrementPraiseCountByuserId(authorId,1);
        //4.增加活跃度
        activityServiceImpl.addPraiseActivity(articleId, userId,praiseVo.getCommentId());
    }

    /**
     * 用户取消点赞
     * @param praiseVo
     */
    @Override
    public void cancelPraise(PraiseVo praiseVo) {
        //1.article_praise减少关系
        Long articleId = praiseVo.getArticleId();
        Long userId = ThreadLocalContext.getUserId();
        //1.1如果已经没有数据 则该取消点赞无效
        LambdaQueryWrapper<ArticlePraise> articlePraiseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articlePraiseLambdaQueryWrapper.eq(ArticlePraise::getArticleId,articleId);
        articlePraiseLambdaQueryWrapper.eq(ArticlePraise::getUserId, userId);
        if(articlePraiseMapper.selectOne(articlePraiseLambdaQueryWrapper) == null){
            //无数据
            return;
        }
        // 假设你有一个 ArticlePraise 实体类和 articlePraiseMapper，进行删除操作
        Map<String, Object> map = new HashMap<>();
        map.put("article_id", articleId);  // 设置文章ID
        map.put("user_id", userId);        // 设置用户ID

        // 使用 deleteByMap 方法
        articlePraiseMapper.deleteByMap(map);
        //2.文章表点赞数-1
        articleMapper.cancelPraise(articleId);
        //3.文章作者的被点赞数-1
        LambdaQueryWrapper<Article> articleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleLambdaQueryWrapper.eq(Article::getId,articleId);
        Article article = articleMapper.selectOne(articleLambdaQueryWrapper);
        Long authorId = article.getUserId();
        userInfoMapper.decreasePraiseCountByuserId(authorId,-1);
        //4.取消活跃度
        activityServiceImpl.cancelPraiseActivity(articleId, userId,praiseVo.getCommentId());
    }


    /**
     * 查询全部文章
     * @return
     */
    @Override
    public List<ArticleUserVo> selectAll() {
        log.info("查询全部文章");
        //1.根据文章id查询文章
        List<ArticleUserVo> articleUserVoList = articleUserMapper.queryAll();
        for (ArticleUserVo articleUserVo : articleUserVoList){
            //2.查询文章评论数
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("article_id", articleUserVo.getId());

            Long commentNumber = commentMapper.selectCount(queryWrapper);
            if(commentNumber != null)
                articleUserVo.setCommentNumber(commentNumber);
            else{
                articleUserVo.setCommentNumber(0L);
            }
        }
        return articleUserVoList;
    }

    @Override
    public List<ArticleUserVo> getArticleUserCollectionByUserId() {
        //1.根据用户id查询用户收藏的文章的Id
        Long userId = ThreadLocalContext.getUserId();
        List<Long> articleIdList = articleCollectionMapper.getArticleUserCollectionByUserId(userId);
        //2.根据文章id查询出文章
        List<ArticleUserVo> articleUserVoList = new ArrayList<>();
        for (Long articleId : articleIdList){
            ArticleUserVo articleUserVoById = getArticleUserVoById(articleId);
            articleUserVoList.add(articleUserVoById);
        }
        return articleUserVoList;
    }

    /**
     * 根据UserId查询一段时间内的所有浏览记录
     * @param browseHistoryVo
     * @return
     */
    @Override
    public List<ArticleUserVo> getBorwseHistoryWithTime(BrowseHistoryVo browseHistoryVo) {
        //1.根据用户id查询其一段时间内的浏览记录
        Long userId = ThreadLocalContext.getUserId();
        browseHistoryVo.setUserId(userId);
        List<Long> articleIdList = readHistoryMapper.queryBrowseHistory(browseHistoryVo);
        //2.根据文章id查询文章信息
        List<ArticleUserVo> articleUserVoList = new ArrayList<>();
        for(Long articleId : articleIdList){
            ArticleUserVo articleUserVoById = getArticleUserVoById(articleId);
            articleUserVoList.add(articleUserVoById);
        }
        return articleUserVoList;
    }

    /**
     * 用户收藏文章
     * @param articleCollection
     */
    @Override
    public void collection(ArticleCollection articleCollection) {
        //1.保存收藏信息
        //1.1判断是否已经收藏了
        Long userId = ThreadLocalContext.getUserId();
        Long articleId = articleCollection.getArticleId();
        LambdaQueryWrapper<ArticleCollection> articleCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleCollectionLambdaQueryWrapper.eq(ArticleCollection::getArticleId,articleId);
        articleCollectionLambdaQueryWrapper.eq(ArticleCollection::getUserId,userId);
        if(articleCollectionMapper.selectOne(articleCollectionLambdaQueryWrapper) != null){
            //已经收藏了
            return;
        }
        articleCollection.setUserId(userId);
        articleCollection.setCreateTime(LocalDateTime.now());
        articleCollection.setUpdateTime(LocalDateTime.now());
        articleCollectionMapper.insert(articleCollection);
        //2.文章作者的被收藏数+1
        Article article = articleMapper.selectById(articleId);
        Long authorId = article.getUserId();
        userInfoMapper.incrementCollectionCountByUserId(authorId,1);
        //3.文章被收藏数加+1
        article.setCollection(article.getCollection() + 1);
        articleMapper.updateById(article);
        //4.活跃度增加 +3分
        ActivityVo activityVo = new ActivityVo();
        activityVo.setArticleId(articleId);
        activityVo.setUserId(userId);
        activityVo.setStatusEnums(StatusEnums.COLLECTION);
        rabbitTemplate.convertAndSend(RabbitmqConfig.ACTIVITY_DIRECT,RabbitmqConfig.ACTIVITY_BINGING,activityVo);
    }

    /**
     * 用户收藏文章
     * @param articleCollection
     */
    @Override
    public void cannelCollection(ArticleCollection articleCollection) {
        //1.取消保存收藏信息
        //1.1判断是否已经收藏了
        Long userId = ThreadLocalContext.getUserId();
        Long articleId = articleCollection.getArticleId();
        LambdaQueryWrapper<ArticleCollection> articleCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleCollectionLambdaQueryWrapper.eq(ArticleCollection::getArticleId,articleId);
        articleCollectionLambdaQueryWrapper.eq(ArticleCollection::getUserId,userId);
        if(articleCollectionMapper.selectOne(articleCollectionLambdaQueryWrapper) == null){
            //没有收藏
            return;
        }
        articleCollection.setUserId(userId);
        articleCollection.setCreateTime(LocalDateTime.now());
        articleCollection.setUpdateTime(LocalDateTime.now());
        articleCollectionMapper.deleteCollection(articleCollection);
        //2.文章作者的被收藏数-1
        Article article = articleMapper.selectById(articleId);
        Long authorId = article.getUserId();
        userInfoMapper.decreaseCollectionCountByUserId(authorId,-1);
        //3.文章被收藏数加-1
        article.setCollection(article.getCollection() - 1);
        articleMapper.updateById(article);
        //4.活跃度增加 +3分
        ActivityVo activityVo = new ActivityVo();
        activityVo.setArticleId(articleId);
        activityVo.setUserId(userId);
        activityVo.setStatusEnums(StatusEnums.DECREASE_COLLECTION);
        rabbitTemplate.convertAndSend(RabbitmqConfig.ACTIVITY_DIRECT,RabbitmqConfig.ACTIVITY_BINGING,activityVo);
    }

    /**
     * 保存或更新文章
     * @param req    上传的文章体
     * @param author 作者
     * @return
     */
    @Override
    public ArticleUserVo saveArticle(Article req, Long author) {
        //请请求体转为数据库实体
        Article article = ArticleConverter.toArticle(req, author);
        //将图片转为url链接
        if(article != null)
            article.setPicture(article.getPicture());

        return transactionTemplate.execute(new TransactionCallback<ArticleUserVo>() {
            @Override
            public ArticleUserVo doInTransaction(TransactionStatus status) {
                Long articleId;
                if (NumUtil.nullOrZero(req.getId())) {
                    //1.增加发布文章数
                    userInfoMapper.incrementPublishCount(req.getUserId(),1);
                    //2.保存文章
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
     * 删除文章
     * @param articleId   文章id
     * @param loginUserId 执行操作的用户
     */
    @Override
    public void deleteArticle(Long articleId, Long loginUserId) {
        //1.先确认是作者本人执行的删除操作
        Article article = articleMapper.selectById(articleId);
        if(article.getUserId() != loginUserId){
            return;
        }
        //2.先删除用户浏览记录表里面此文章的记录
        LambdaQueryWrapper<ReadHistory> readHistoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        readHistoryLambdaQueryWrapper.eq(ReadHistory::getArticleId,articleId);
        readHistoryMapper.delete(readHistoryLambdaQueryWrapper);
        //3.作者发布文章数-1
        userInfoMapper.decreasePublishCount(loginUserId,-1);
        //4.删除文章的评论
        LambdaQueryWrapper<Comment> commentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentLambdaQueryWrapper.eq(Comment::getArticleId,articleId);
        List<Comment> comments = commentMapper.selectList(commentLambdaQueryWrapper);
        commentMapper.delete(commentLambdaQueryWrapper);
        //5.删除文章的评论到的点赞表
        for (Comment comment : comments){
            LambdaQueryWrapper<CommentPraise> commentPraiseLambdaQueryWrapper = new LambdaQueryWrapper<>();
            commentPraiseLambdaQueryWrapper.eq(CommentPraise::getCommentId,comment.getId());
            commentPraiseMapper.delete(commentPraiseLambdaQueryWrapper);
        }
        //6.删除文章的点赞表
        LambdaQueryWrapper<ArticlePraise> articlePraiseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articlePraiseLambdaQueryWrapper.eq(ArticlePraise::getArticleId,articleId);
        articlePraiseMapper.delete(articlePraiseLambdaQueryWrapper);
        //7.删除文章的收藏表
        LambdaQueryWrapper<ArticleCollection> articleCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleCollectionLambdaQueryWrapper.eq(ArticleCollection::getArticleId,articleId);
        articleCollectionMapper.delete(articleCollectionLambdaQueryWrapper);
        //8.删除文章
        articleMapper.deleteById(articleId);
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
    public void praiseComment(PraiseVo praiseVo){
        //1.comment_praise增加关系
        Long commentId = praiseVo.getCommentId();
        Long userId = ThreadLocalContext.getUserId();
        //1.1如果已经有数据 则该点赞无效
        LambdaQueryWrapper<CommentPraise> articlePraiseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articlePraiseLambdaQueryWrapper.eq(CommentPraise::getCommentId,commentId);
        articlePraiseLambdaQueryWrapper.eq(CommentPraise::getUserId, userId);
        if(commentPraiseMapper.selectOne(articlePraiseLambdaQueryWrapper) != null){
            //有数据
            return;
        }
        CommentPraise commentPraise = new CommentPraise();
        commentPraise.setCommentId(commentId);
        commentPraise.setUpdateTime(LocalDateTime.now());
        commentPraise.setCreateTime(LocalDateTime.now());
        commentPraise.setUserId(userId);
        commentPraiseMapper.insert(commentPraise);
        //2.增加活跃度
        activityServiceImpl.addPraiseActivity(praiseVo.getArticleId(), userId,praiseVo.getCommentId());
        //3.评论表点赞数+1
        UpdateWrapper<Comment> commentUpdateWrapper = new UpdateWrapper<>();
        commentUpdateWrapper.eq("id",commentId);
        commentUpdateWrapper.setSql("praise = praise + 1");
        commentMapper.update(commentUpdateWrapper);
    }

    @Override
    public void cancelPraiseComment(PraiseVo praiseVo){
        //1.comment_praise增加关系
        Long commentId = praiseVo.getCommentId();
        Long userId = ThreadLocalContext.getUserId();
        //1.1如果没有数据 则该取消点赞无效
        LambdaQueryWrapper<CommentPraise> articlePraiseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articlePraiseLambdaQueryWrapper.eq(CommentPraise::getCommentId,commentId);
        articlePraiseLambdaQueryWrapper.eq(CommentPraise::getUserId, userId);
        if(commentPraiseMapper.selectOne(articlePraiseLambdaQueryWrapper) == null){
            //无数据
            return;
        }
        LambdaQueryWrapper<CommentPraise> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(CommentPraise::getCommentId,commentId)
                        .eq(CommentPraise::getUserId,userId);
        commentPraiseMapper.delete(lambdaQueryWrapper);
        //2.减少活跃度
        activityServiceImpl.cancelPraiseActivity(praiseVo.getArticleId(), userId,praiseVo.getCommentId());
        //3.评论表点赞数-1
        //3.评论表点赞数+1
        UpdateWrapper<Comment> commentUpdateWrapper = new UpdateWrapper<>();
        commentUpdateWrapper.eq("id",commentId);
        commentUpdateWrapper.setSql("praise = praise - 1");
        commentMapper.update(commentUpdateWrapper);
    }

    /**
     * 模糊查询
     * @param key
     * @return
     */
    @Override
    public List<ArticleUserVo> searchByKey(String key){
        //1.根据key去模糊title查询
        List<ArticleUserVo> articleUserVoList = articleUserMapper.searchByKey(key);
        //2.查询评论数
        //2.查询文章评论数
        for (ArticleUserVo articleUserVo : articleUserVoList){
            QueryWrapper queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("article_id", articleUserVo.getId());
            Long commentNumber = commentMapper.selectCount(queryWrapper);
            articleUserVo.setCommentNumber(commentNumber != null ? commentNumber : 0);
        }
        return articleUserVoList;
    }
}

















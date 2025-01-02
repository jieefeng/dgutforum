package com.dgutforum.article.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dgutforum.activity.service.impl.ActivityServiceImpl;
import com.dgutforum.article.entity.ArticleCollection;
import com.dgutforum.article.entity.ArticlePraise;
import com.dgutforum.article.entity.ReadHistory;
import com.dgutforum.article.vo.PraiseVo;
import com.dgutforum.article.req.ArticleUserIdReq;
import com.dgutforum.article.vo.ArticleUserVo;
import com.dgutforum.article.entity.Article;
import com.dgutforum.article.vo.BrowseHistoryVo;
import com.dgutforum.article.service.ArticleWriteService;
import com.dgutforum.comment.entity.CommentPraise;
import com.dgutforum.common.result.Result;
import com.dgutforum.context.ThreadLocalContext;
import com.dgutforum.mapper.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/article")
@Tag(name = "文章相关接口")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleWriteService articleWriteService;
    private final ArticleMapper articleMapper;
    private final ReadHistoryMapper readHistoryMapper;
    private final RabbitTemplate rabbitTemplate;
    private final ActivityServiceImpl activityServiceImpl;
    private final ArticleCollectionMapper articleCollectionMapper;
    private final UserInfoMapper userInfoMapper;
    private final ArticlePraiseMapper articlePraiseMapper;
    private final CommentPraiseMapper commentPraiseMapper;

    /**
     * 查询全部文章
     * @return
     */
    @GetMapping("")
    @Operation(summary = "查询全部文章")
    public Result getAll(){
        List<ArticleUserVo> articleUserVoList = articleWriteService.selectAll();
        return Result.success(articleUserVoList);
    }

    /**
     * 发布文章，完成后返回文章
     * @return
     */
    @PostMapping(path = "save")
    @Operation(summary = "发布文章，完成后返回文章")
    public Result post(@RequestBody Article article) throws IOException {
        log.info("发布文章:{}",article);
        ArticleUserVo articleUserVo = articleWriteService.saveArticle(article, article.getUserId());
        return Result.success(articleUserVo);
    }

    /**
     * 根据文章id查询文章
     * @param articleId
     * @return
     */
    @GetMapping(path = "{articleId}")
    @Operation(summary = "根据文章id查询文章")
    public Result getArticleByArticleId(@PathVariable Long articleId){
        //1.记录浏览记录
        //1.1先查询是否存在
        Long userId = ThreadLocalContext.getUserId();
        ReadHistory readHistory = new ReadHistory();
        readHistory.setArticleId(articleId);
        readHistory.setUserId(userId);
        readHistory.setReadTime(LocalDateTime.now());
        readHistory.setCreateTime(LocalDateTime.now());
        readHistory.setUpdateTime(LocalDateTime.now());
        ReadHistory query = readHistoryMapper.query(readHistory);
        if(query == null){
            //1.2记录浏览记录
            readHistoryMapper.insert(readHistory);
        }
        //2.增加活跃度
        activityServiceImpl.addReadActivity(articleId, userId);
        //3.查询文章
        ArticleUserVo articleUserVoById = articleWriteService.getArticleUserVoById(articleId);
        //4.增加文章阅读数 +1
        Article article = new Article();
        article.setId(articleUserVoById.getId());
        article.setReadCount(articleUserVoById.getReadCount() + 1);
        articleMapper.updateById(article);
        //5.增加用户阅读文章数
        userInfoMapper.incrementReadCountByuserId(userId);
        return Result.success(articleUserVoById);
    }



    /**
     * 根据分类id查询文章
     * @param categoryId
     * @return
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类id查询文章")
    public Result getByCategoryId(@PathVariable Long categoryId){
        List<ArticleUserVo> byCategoryId = articleWriteService.getByCategoryId(categoryId);
        return Result.success(byCategoryId);
    }

//    /**
//     * 根据文章id删除文章  1代表删除成功 0代表删除失败
//     * @param articleId
//     * @return
//     */
//    @DeleteMapping("/delete/{articleId}")
//    public ResVo<Long> delete(@PathVariable Long articleId){
//        int result = articleMapper.deleteById(articleId);
//        if (result > 0){
//            return ResVo.ok(1L);
//        } else {
//            return ResVo.ok(0L);
//        }
//    }



    /**
     * 根据文章id更新文章或者删除
     * @param article
     * @return
     */
    @PostMapping(path = "update")
    @Operation(summary = "根据文章id更新文章或者删除")
    public Result deleteArticleByArticleId(@RequestBody Article article){
        boolean success = articleWriteService.updateById(article);
        if(success){
            return Result.success(1L);
        } else {
            return Result.error("参数异常");
        }
    }

    /**
     * 根据用户id查询文章
     * @param articleUserIdReq
     * @return
     */
    @PostMapping("get")
    @Operation(summary = "根据用户id查询文章")
    public Result getArticleUserByArticleId(@RequestBody ArticleUserIdReq articleUserIdReq){
        PraiseVo praiseVo = new PraiseVo();
        praiseVo.setUserId(articleUserIdReq.getUserId());
        return Result.success(articleWriteService.getArticleUserByArticleId(praiseVo));
    }

    /**
     * 根据用户id查询用户收藏的文章
     * @return
     */
    @GetMapping("/getArticleUserCollection")
    @Operation(summary = "根据用户id查询用户收藏的文章")
    public Result getArticleUserCollectionByUserId(){
        List<ArticleUserVo> articleUserVoList = articleWriteService.getArticleUserCollectionByUserId();
        return Result.success(articleUserVoList);
    }



    /**
     * 根据用户id查询点赞过的文章
     * @param praiseVo
     * @return
     */
    @PostMapping("getPraise")
    @Operation(summary = "根据用户id查询点赞列表")
    public Result getArticleUserPraiseByUserId(@RequestBody PraiseVo praiseVo){
        return Result.success(articleWriteService.getArticleUserPraiseByUserId(praiseVo));
    }


    /**
     * 用户点赞文章
     * @param praiseVo
     * @return
     */
    @PostMapping("praise")
    @Operation(summary = "用户点赞")
    public Result praise(@RequestBody PraiseVo praiseVo){
        articleWriteService.praise(praiseVo);
        return Result.success();
    }

    /**
     * 用户是否点赞文章
     * @param praiseVo
     * @return
     */
    @PostMapping("isPraise")
    @Operation(summary = "用户是否点赞")
    public Result isPraise(@RequestBody PraiseVo praiseVo){
        ArticlePraise articlePraise = new ArticlePraise();
        LambdaQueryWrapper<ArticlePraise> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticlePraise::getArticleId,praiseVo.getArticleId())
                    .eq(ArticlePraise::getUserId,ThreadLocalContext.getUserId());
        ArticlePraise articlePraise1 = articlePraiseMapper.selectOne(queryWrapper);
        if(articlePraise1 == null){
            //用户未点赞文章
            return Result.success(false);
        } else {
            return Result.success(true);
        }
    }

    /**
     * 取消点赞
     * @param praiseVo
     * @return
     */
    @PostMapping("/cancelPraise")
    @Operation(summary = "取消点赞")
    public Result cancelPraise(@RequestBody PraiseVo praiseVo){
        articleWriteService.cancelPraise(praiseVo);
        return Result.success();
    }


    /**
     * 用户收藏文章
     * @param articleCollection
     * @return
     */
    @Operation(summary = "用户收藏文章")
    @PostMapping("/collection")
    public Result collection(@RequestBody ArticleCollection articleCollection){
        articleWriteService.collection(articleCollection);
        return Result.success();
    }

    /**
     * 用户是否收藏文章
     * @param articleCollection
     * @return
     */
    @Operation(summary = "用户是否收藏文章")
    @PostMapping("/isCollection")
    public Result isCollection(@RequestBody ArticleCollection articleCollection){
        LambdaQueryWrapper<ArticleCollection> articleCollectionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        articleCollectionLambdaQueryWrapper.eq(ArticleCollection::getArticleId,articleCollection.getArticleId())
                        .eq(ArticleCollection::getUserId,ThreadLocalContext.getUserId());
        ArticleCollection articleCollection1 = articleCollectionMapper.selectOne(articleCollectionLambdaQueryWrapper);
        if(articleCollection1 == null){
            //用户未收藏
            return Result.success(false);
        } else {
            return Result.success(true);
        }
    }

    /**
     * 取消收藏
     * @param articleCollection
     * @return
     */
    @PostMapping("/cancelCollection")
    @Operation(summary = "取消收藏")
    public Result cancelCollection(@RequestBody ArticleCollection articleCollection){
        articleWriteService.cannelCollection(articleCollection);
        return Result.success();
    }


    /**
     * 根据用户Id一段内他的浏览记录
     * @param browseHistoryVo
     * @return
     */
    @PostMapping("getReadHistoryWithTime")
    @Operation(summary = "根据用户Id一段内他的浏览记录")
    public Result getReadHistoryWithTime(@RequestBody BrowseHistoryVo browseHistoryVo){
        List<ArticleUserVo> borwseHistoryWithTime = articleWriteService.getBorwseHistoryWithTime(browseHistoryVo);
        return Result.success(borwseHistoryWithTime);
    }

    /**
     * 点赞评论
     * @param praiseVo
     * @return
     */
    @PostMapping("praiseComment")
    @Operation(summary = "点赞评论")
    public Result praiseComment(@RequestBody PraiseVo praiseVo){
        articleWriteService.praiseComment(praiseVo);
        return Result.success();
    }

    /**
     * 取消点赞评论
     * @param praiseVo
     * @return
     */
    @PostMapping("cancelPraiseComment")
    @Operation(summary = " 取消点赞评论")
    public Result cancelPraiseComment(@RequestBody PraiseVo praiseVo){
        articleWriteService.cancelPraiseComment(praiseVo);
        return Result.success();
    }

    /**
     * 用户是否给评论点赞
     * @param praiseVo
     * @return
     */
    @PostMapping("/isPraiseComment")
    @Operation(summary = "是否点赞评论")
    public Result isPraiseComment(@RequestBody PraiseVo praiseVo){
        LambdaQueryWrapper<CommentPraise> commentPraiseLambdaQueryWrapper = new LambdaQueryWrapper<>();
        commentPraiseLambdaQueryWrapper.eq(CommentPraise::getCommentId,praiseVo.getCommentId())
                        .eq(CommentPraise::getUserId,ThreadLocalContext.getUserId());
        CommentPraise commentPraise = commentPraiseMapper.selectOne(commentPraiseLambdaQueryWrapper);
        if(commentPraise == null){
            return Result.success(false);
        } else {
            return Result.success(true);
        }
    }

    /**
     * 删除文章
     * @param article
     * @return
     */
    @PostMapping("/article")
    @Operation(summary = "删除文章")
    public Result deleteArticle(@RequestBody Article article){
        articleWriteService.deleteArticle(article.getId(),ThreadLocalContext.getUserId());
        return Result.success();
    }


    /**
     * 根据key查询
     * @param key
     * @return
     */
    @GetMapping("/search/{key}")
    @Operation(summary = "模糊查询")
    public Result search(@PathVariable String key){
        List<ArticleUserVo> articleUserVoList = articleWriteService.searchByKey(key);
        return Result.success(articleUserVoList);
    }
}






































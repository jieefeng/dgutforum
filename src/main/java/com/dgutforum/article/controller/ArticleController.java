package com.dgutforum.article.controller;

import com.dgutforum.activity.service.ActivityService;
import com.dgutforum.article.entity.ArticleCollection;
import com.dgutforum.article.entity.ReadHistory;
import com.dgutforum.article.vo.PraiseVo;
import com.dgutforum.article.req.ArticleUserIdReq;
import com.dgutforum.article.vo.ArticleUserVo;
import com.dgutforum.article.entity.Article;
import com.dgutforum.article.vo.BrowseHistoryVo;
import com.dgutforum.article.service.ArticleWriteService;
import com.dgutforum.article.req.ArticlePostReq;
import com.dgutforum.common.result.Result;
import com.dgutforum.context.ThreadLocalContext;
import com.dgutforum.mapper.ArticleCollectionMapper;
import com.dgutforum.mapper.ArticleMapper;
import com.dgutforum.mapper.ReadHistoryMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ActivityService activityService;
    private final ArticleCollectionMapper articleCollectionMapper;

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
    public Result post(@RequestBody ArticlePostReq req) throws IOException {
        log.info("发布文章:{}",req);
        ArticleUserVo articleUserVo = articleWriteService.saveArticle(req, req.getUserId());
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
        activityService.addReadActivity(articleId, userId);
        //3.查询文章
        ArticleUserVo articleUserVoById = articleWriteService.getArticleUserVoById(articleId);
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
        return Result.success(articleWriteService.getByCategoryId(categoryId));
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
     * 用户点赞 如果传入了commentId就是对评论 否则是对评论点赞
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


}






































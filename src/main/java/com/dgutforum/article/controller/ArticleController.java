package com.dgutforum.article.controller;

import com.dgutforum.activity.service.ActivityService;
import com.dgutforum.article.entity.ReadHistory;
import com.dgutforum.article.req.praiseVo;
import com.dgutforum.article.req.ArticleUserIdReq;
import com.dgutforum.article.vo.ArticleUserVo;
import com.dgutforum.article.entity.Article;
import com.dgutforum.common.result.ResVo;
import com.dgutforum.article.service.ArticleWriteService;
import com.dgutforum.article.req.ArticlePostReq;
import com.dgutforum.common.result.eunms.StatusEnum;
import com.dgutforum.context.ThreadLocalContext;
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

    /**
     * 查询全部文章
     * @return
     */
    @GetMapping("")
    public ResVo<List<ArticleUserVo>> getAll(){
        List<ArticleUserVo> articleUserVoList = articleWriteService.selectAll();
        return ResVo.ok(articleUserVoList);
    }

    /**
     * 发布文章，完成后返回文章
     * @return
     */
    @PostMapping(path = "save")
    @Operation(summary = "发布文章，完成后返回文章")
    public ResVo<ArticleUserVo> post(@RequestBody ArticlePostReq req) throws IOException {
        log.info("发布文章:{}",req);
        ArticleUserVo articleUserVo = articleWriteService.saveArticle(req, req.getUserId());
        return ResVo.ok(articleUserVo);
    }

    /**
     * 根据文章id查询文章
     * @param articleId
     * @return
     */
    @GetMapping(path = "{articleId}")
    @Operation(summary = "根据文章id查询文章")
    public ResVo<ArticleUserVo> getArticleByArticleId(@PathVariable Long articleId){
        //1.记录浏览记录
        Long userId = ThreadLocalContext.getUserId();
        ReadHistory readHistory = new ReadHistory();
        readHistory.setArticleId(articleId);
        readHistory.setUserId(userId);
        readHistory.setReadTime(LocalDateTime.now());
        readHistory.setCreateTime(LocalDateTime.now());
        readHistory.setUpdateTime(LocalDateTime.now());
        readHistoryMapper.save(readHistory);
        //2.增加活跃度
        activityService.addReadActivity(articleId, userId);
        return ResVo.ok(articleWriteService.getArticleUserVoById(articleId));
    }



    /**
     * 根据分类id查询文章
     * @param categoryId
     * @return
     */
    @GetMapping("/category/{categoryId}")
    @Operation(summary = "根据分类id查询文章")
    public ResVo<List<ArticleUserVo>> getByCategoryId(@PathVariable Long categoryId){
        return ResVo.ok(articleWriteService.getByCategoryId(categoryId));
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
    public ResVo<Long> deleteArticleByArticleId(@RequestBody Article article){
        boolean success = articleWriteService.updateById(article);
        if(success){
            return ResVo.ok(1L);
        } else {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED);
        }
    }

    /**
     * 根据用户id查询文章
     * @param articleUserIdReq
     * @return
     */
    @PostMapping("get")
    @Operation(summary = "根据用户id查询文章")
    public ResVo<List<ArticleUserVo>> getArticleUserByArticleId(@RequestBody ArticleUserIdReq articleUserIdReq){
        praiseVo praiseVo = new praiseVo();
        praiseVo.setUserId(articleUserIdReq.getUserId());
        return ResVo.ok(articleWriteService.getArticleUserByArticleId(praiseVo));
    }

    /**
     * 根据用户id查询用户收藏的文章
     * @return
     */
    @GetMapping("/getArticleUserCollection")
    public ResVo<List<ArticleUserVo>> getArticleUserCollectionByUserId(){
        List<ArticleUserVo> articleUserVoList = articleWriteService.getArticleUserCollectionByUserId();
        return ResVo.ok(articleUserVoList);
    }

    /**
     * 根据用户id查询点赞过的文章
     * @param praiseVo
     * @return
     */
    @PostMapping("getPraise")
    @Operation(summary = "根据用户id查询点赞列表")
    public ResVo<List<ArticleUserVo>> getArticleUserPraiseByUserId(@RequestBody praiseVo praiseVo){
        return ResVo.ok(articleWriteService.getArticleUserPraiseByUserId(praiseVo));
    }


    /**
     * 用户点赞 如果传入了commentId就是对评论 否则是对评论点赞
     * @param praiseVo
     * @return
     */
    @PostMapping("praise")
    @Operation(summary = "用户点赞")
    public ResVo praise(@RequestBody praiseVo praiseVo){
        articleWriteService.praise(praiseVo);
        return ResVo.ok(null);
    }
}






































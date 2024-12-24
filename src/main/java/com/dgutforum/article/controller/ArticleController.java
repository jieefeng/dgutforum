package com.dgutforum.article.controller;

import com.dgutforum.article.req.ArticleGetReq;
import com.dgutforum.article.req.ArticleUserIdReq;
import com.dgutforum.article.vo.ArticleUserVo;
import com.dgutforum.article.entity.Article;
import com.dgutforum.common.result.ResVo;
import com.dgutforum.article.service.ArticleWriteService;
import com.dgutforum.article.req.ArticlePostReq;
import com.dgutforum.common.result.eunms.StatusEnum;
import com.dgutforum.mapper.ArticleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/article")
@Tag(name = "文章相关接口")
public class ArticleController {

    @Resource
    private ArticleWriteService articleWriteService;

    @Resource
    private ArticleMapper articleMapper;

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
        ArticleGetReq articleGetReq = new ArticleGetReq();
        articleGetReq.setUserId(articleUserIdReq.getUserId());
        return ResVo.ok(articleWriteService.getArticleUserByArticleId(articleGetReq));
    }

    public ResVo<List<ArticleUserVo>> getArticleUserCollectionByUserId(){

        return null;
    }

    /**
     * 根据用户id查询点赞列表
     * @param articleGetReq
     * @return
     */
    @PostMapping("getPraise")
    @Operation(summary = "根据用户id查询点赞列表")
    public ResVo<List<ArticleUserVo>> getArticleUserPraiseByUserId(@RequestBody ArticleGetReq articleGetReq){
        return ResVo.ok(articleWriteService.getArticleUserPraiseByUserId(articleGetReq));
    }


    /**
     * 用户点赞
     * @param articleGetReq
     * @return
     */
    @PostMapping("praise")
    @Operation(summary = "用户点赞")
    public ResVo praise(@RequestBody ArticleGetReq articleGetReq){
        articleWriteService.praise(articleGetReq);
        return ResVo.ok(null);
    }


}






































package com.dgutforum.article.controller;

import com.dgutforum.article.vo.ArticleUserVo;
import com.dgutforum.article.entity.Article;
import com.dgutforum.common.result.ResVo;
import com.dgutforum.article.service.ArticleWriteService;
import com.dgutforum.article.req.ArticlePostReq;
import com.dgutforum.common.result.eunms.StatusEnum;
import com.dgutforum.mapper.ArticleMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleWriteService articleWriteService;

    @Resource
    private ArticleMapper articleMapper;

    /**
     * 发布文章，完成后返回文章
     * @return
     */
    @PostMapping(path = "save")
    public ResVo<ArticleUserVo> post(@RequestBody ArticlePostReq req) throws IOException {
        ArticleUserVo articleUserVo = articleWriteService.saveArticle(req, 8L/*ReqInfoContext.getReqInfo().getUserId()*/);
        return ResVo.ok(articleUserVo);
    }

    /**
     * 根据文章id查询文章
     * @param articleId
     * @return
     */
    @GetMapping(path = "{articleId}")
    public ResVo<Article> getArticleByArticleId(@PathVariable Long articleId){
        return ResVo.ok(articleWriteService.getById(articleId));
    }

    /**
     * 根据分类id查询文章
     * @param categoryId
     * @return
     */
    @GetMapping("/category/{categoryId}")
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
    public ResVo<Long> deleteArticleByArticleId(@RequestBody Article article){
        boolean success = articleWriteService.updateById(article);
        if(success){
            return ResVo.ok(1L);
        } else {
            return ResVo.fail(StatusEnum.ILLEGAL_ARGUMENTS_MIXED);
        }
    }

}






































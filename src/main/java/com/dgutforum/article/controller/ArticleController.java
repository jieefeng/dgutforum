package com.dgutforum.article.controller;

import com.dgutforum.article.entity.Article;
import com.dgutforum.common.result.ResVo;
import com.dgutforum.article.service.ArticleWriteService;
import com.dgutforum.article.vo.ArticlePostReq;
import com.dgutforum.common.result.eunms.StatusEnum;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleWriteService articleWriteService;

    /**
     * TODO 发布文章，完成后跳转到详情页 这里有一个重定向的知识点
     * @return
     */
    @PostMapping(path = "save")
    public ResVo<Long> post(@RequestBody ArticlePostReq req) throws IOException {
        Long id = articleWriteService.saveArticle(req, 8L/*ReqInfoContext.getReqInfo().getUserId()*/);
        // 如果使用后端重定向，可以使用下面两种策略
//        return "redirect:/article/detail/" + id;
//        response.sendRedirect("/article/detail/" + id);
        // 这里采用前端重定向策略
        return ResVo.ok(id);
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






































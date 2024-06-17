package com.dgutforum.article.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.QueryChainWrapper;
import com.dgutforum.article.Do.ArticleUserDo;
import com.dgutforum.article.dto.ArticleUserDto;
import com.dgutforum.article.entity.Article;
import com.dgutforum.common.result.ResVo;
import com.dgutforum.article.service.ArticleWriteService;
import com.dgutforum.article.vo.ArticlePostReq;
import com.dgutforum.common.result.eunms.StatusEnum;
import com.dgutforum.mapper.ArticleMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import static com.dgutforum.common.result.eunms.StatusEnum.ARTICLE_NOT_EXISTS;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleWriteService articleWriteService;

    @Resource
    private ArticleMapper articleMapper;

    /**
     * TODO 发布文章，完成后跳转到详情页 这里有一个重定向的知识点
     * @return
     */
    @PostMapping(path = "save")
    public ResVo<ArticleUserDo> post(@RequestBody ArticlePostReq req) throws IOException {
        ArticleUserDo articleUserDo = articleWriteService.saveArticle(req, 8L/*ReqInfoContext.getReqInfo().getUserId()*/);
        // 如果使用后端重定向，可以使用下面两种策略
//        return "redirect:/article/detail/" + id;
//        response.sendRedirect("/article/detail/" + id);
        // 这里采用前端重定向策略
        return ResVo.ok(articleUserDo);
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
    public ResVo<List<ArticleUserDo>> getByCategoryId(@PathVariable Long categoryId){
        return ResVo.ok(articleWriteService.getByCategoryId(categoryId));
    }

    /**
     * 根据文章id删除文章  1代表删除成功 0代表删除失败
     * @param articleId
     * @return
     */
    @DeleteMapping("/delete/{articleId}")
    public ResVo<Long> delete(@PathVariable Long articleId){
        int result = articleMapper.deleteById(articleId);
        if (result > 0){
            return ResVo.ok(1L);
        } else {
            return ResVo.ok(0L);
        }
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






































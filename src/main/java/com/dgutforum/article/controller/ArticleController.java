package com.dgutforum.article.controller;

import com.dgutforum.common.result.ResVo;
import com.dgutforum.article.service.ArticleWriteService;
import com.dgutforum.article.vo.ArticlePostReq;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleWriteService articleWriteService;

    /**
     * 发布文章，完成后跳转到详情页
     * - 这里有一个重定向的知识点
     * @return
     */
    @PostMapping(path = "post")
    public ResVo<Long> post(@RequestBody ArticlePostReq req, HttpServletResponse response) throws IOException {
        Long id = articleWriteService.saveArticle(req, 1L/*ReqInfoContext.getReqInfo().getUserId()*/);
        // 如果使用后端重定向，可以使用下面两种策略
//        return "redirect:/article/detail/" + id;
//        response.sendRedirect("/article/detail/" + id);
        // 这里采用前端重定向策略
        return ResVo.ok(id);
    }

}

package com.dgutforum.user.controller;

import com.dgutforum.article.req.ArticleUserIdReq;
import com.dgutforum.article.vo.ArticleBasicInfoVo;
import com.dgutforum.article.vo.ArticleUserVo;
import com.dgutforum.article.vo.PraiseVo;
import com.dgutforum.common.result.Result;
import com.dgutforum.user.service.UserArticleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserArticleController {

    @Autowired
    UserArticleService userArticleService;


    @GetMapping("/article/{id}")
    public Result getArticleUserByUserId(@PathVariable long id){
        log.info("getArticleUserByArticleId id:{}", id);

        List<ArticleUserVo> articleUserVoList = userArticleService.getArticleUserByArticleId(id);

        return Result.success(articleUserVoList);
    }

    @GetMapping("/readhistory/{id}")
    public Result getReadHistoryByArticleId(@PathVariable long id){
        log.info("getReadHistoryByArticleId id:{}", id);

        List<ArticleUserVo> articleUserVoList = userArticleService.getReadHistoryByUserId(id);

        return Result.success(articleUserVoList);
    }

    @GetMapping("/collection/{id}")
    public Result getCollectionByArticleId(@PathVariable long id){
        log.info("getCollectionByArticleId id:{}", id);

        List<ArticleUserVo> articleUserVoList = userArticleService.getCollectionByUserId(id);

        return Result.success(articleUserVoList);
    }

}

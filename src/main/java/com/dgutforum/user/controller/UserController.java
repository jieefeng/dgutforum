package com.dgutforum.user.controller;

import com.dgutforum.common.result.ResVo;
import com.dgutforum.common.result.Result;
import com.dgutforum.user.pojo.Follow;
import com.dgutforum.user.pojo.User;
import com.dgutforum.user.pojo.UserVo;
import com.dgutforum.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 回显界面
     */
    @PostMapping("/get")
    public Result get(@RequestBody User user){
        log.info("要get的id:{}", user.getId());

        User e = userService.get(user);

        return Result.success(e);

    }

    @PostMapping("/update")
    public Result update(@RequestBody User user){
        log.info("user information:{}", user);

        try {
            userService.update(user);
        } catch (Exception e) {
            return Result.error("用户名重复");
        }

        return Result.success();
    }

    @PostMapping("/follow/add")
    public Result follow_add(@RequestBody Follow follow){
        log.info("添加的关注follow:{}", follow);

        userService.follow_add(follow);

        return Result.success();
    }

    @PostMapping("/follow/del")
    public Result follow_del(@RequestBody Follow follow){
        log.info("删除的关注follow:{}", follow);

        userService.follow_del(follow);

        return Result.success();
    }

    @PostMapping("/follow")
    public ResVo<List<UserVo>> follow_select(@RequestBody Follow follow){
        log.info("查询的人的id:{}", follow);

        List<UserVo> e = userService.follow_select(follow);

        return ResVo.ok(e);

    }

}

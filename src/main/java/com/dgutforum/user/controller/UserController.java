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
    public ResVo<User> get(@RequestBody User user){
        log.info("要get的id:{}", user.getId());

        User e = userService.get(user);

//        return Result.success(e);
        return ResVo.ok(e);
    }

    @PostMapping("/update")
    public ResVo update(@RequestBody User user){
        log.info("user information:{}", user);

        try {
            userService.update(user);
        } catch (Exception e) {
            return  ResVo.ok("用户名重复");
        }

        return ResVo.ok(null);
    }

    @PostMapping("/follow/add")
    public ResVo follow_add(@RequestBody Follow follow){
        log.info("添加的关注follow:{}", follow);

        userService.follow_add(follow);

        return ResVo.ok(null);
    }

    @PostMapping("/follow/del")
    public ResVo follow_del(@RequestBody Follow follow){
        log.info("删除的关注follow:{}", follow);

        userService.follow_del(follow);

        return ResVo.ok(null);
    }

    @PostMapping("/follow")
    public ResVo<List<UserVo>> follow_select(@RequestBody Follow follow){
        log.info("查询的人的id:{}", follow);
        List<UserVo> e = userService.follow_select(follow);
        return ResVo.ok(e);
    }

}
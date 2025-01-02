package com.dgutforum.user.controller;

import com.dgutforum.common.result.ResVo;
import com.dgutforum.common.result.Result;
import com.dgutforum.user.pojo.*;
import com.dgutforum.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;


    /**
     * 获取用户基本信息
     */
    @GetMapping("/{id}")
    public Result get(@PathVariable long id){
        log.info("要get的id:{}", id);

        UserVo userVo = userService.get(id);

        return Result.success(userVo);
    }


     /**
     修改用户信息
     */
    @PutMapping()
    public Result update(@RequestBody User user){
        log.info("user information:{}", user);

        boolean flag = userService.update(user);
        if (!flag){
            return Result.error("用户名重复");
        }

        return Result.success();
    }

    /*
    添加关注
     */
    @PostMapping("/follow/add")
    public Result follow_add(@RequestBody Follow follow){
        log.info("添加的关注follow:{}", follow);

        userService.follow_add(follow);

        return Result.success();
    }

    /**
    * 取消关注
    * */
    @PostMapping("/follow/del")
    public Result follow_del(@RequestBody Follow follow){
        log.info("删除的关注follow:{}", follow);

        userService.follow_del(follow);

        return Result.success();
    }

    @PostMapping("/follow/status")
    public Result followStatus(@RequestBody Follow follow){
        log.info("查询关注状态follow:{}", follow);

        boolean flag = userService.followStatus(follow);

        if (flag){
            return Result.success(true);
        }else {
            return Result.success(false);
        }
    }

    /**
    *   粉丝列表
    * */
    @GetMapping("/follower/{id}")
    public Result follower_select(@PathVariable long id){
        log.info("查询的人的id:{}", id);

        List<FollowVo> followVoList = userService.follower_select(id);

        return Result.success(followVoList);
    }

    /**
     *   关注列表
     * */
    @GetMapping("/follow/{id}")
    public Result follow_select(@PathVariable long id){
        log.info("查询的人的id:{}", id);
        List<FollowVo> followVoList = userService.follow_select(id);
        return Result.success(followVoList);
    }

    /*
    * 获取用户文章统计信息
    * */
    @GetMapping("/info/{id}")
    public Result getUserInfo(@PathVariable long id){
        log.info("查询的人的id:{}", id);

        UserInfoVo userInfoVo = userService.getUserInfoByUserId(id);

        return Result.success(userInfoVo);
    }

}

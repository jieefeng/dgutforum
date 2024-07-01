package com.dgutforum.user.controller;

import com.dgutforum.common.result.Result;
import com.dgutforum.user.pojo.User;
import com.dgutforum.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public Result select(@RequestBody User user){
        log.info("查询的username:{}", user.getUsername());

        User e = userService.select(user);

        return Result.success(e);
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user){
        log.info("user information:{}", user);

        userService.update(user);

        return Result.success();
    }

}

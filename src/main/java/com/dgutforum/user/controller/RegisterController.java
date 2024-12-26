package com.dgutforum.user.controller;

import com.dgutforum.common.result.ResVo;
import com.dgutforum.common.result.Result;
import com.dgutforum.user.pojo.User;
import com.dgutforum.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class RegisterController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        log.info("register infomation: {}", user);

        if(user.getPassword() == null || user.getUsername() == null){
            return Result.error("请输入完整");
        }

        User ee = userService.login(user);
        // 先检查用户名是否已存在
        if (ee != null) {
            return Result.error("用户名已存在");
        }

        try {
            // 如果用户名不存在，调用注册方法
            Long userId = userService.register(user);
            userService.registerUserInfo(user.getId());
        } catch (Exception e) {
            // 如果捕获到异常，返回错误提示（可以根据具体的异常类型调整返回内容）
            return Result.error("注册失败");
        }

        return Result.success();
    }

}

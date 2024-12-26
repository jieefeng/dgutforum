package com.dgutforum.user.controller;

import com.dgutforum.common.result.ResVo;
import com.dgutforum.common.result.Result;
import com.dgutforum.common.util.JwtUtils;
import com.dgutforum.user.pojo.User;
import com.dgutforum.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody User user){
        log.info("输入的账号和密码：{}",user);

        if(user.getPassword() == null || user.getUsername() == null){
            return Result.error("请输入完整");
        }

        // 调用登录服务进行验证
        User e = userService.login(user);

        if (e == null) {
            // 用户名不存在的情况
            return Result.error("用户名不存在");
        }

        // 验证密码
        if (!e.getPassword().equals(user.getPassword())) {
            // 密码错误的情况
            return Result.error("密码错误");
        }

        // 登录成功，生成 JWT
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", e.getId());

        String jwt = JwtUtils.generateJwt(claims);
        return Result.success(jwt);
    }
}

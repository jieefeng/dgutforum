package com.dgutforum.user.controller;

import com.dgutforum.common.result.ResVo;
import com.dgutforum.common.result.Result;
import com.dgutforum.common.util.JwtUtils;
import com.dgutforum.user.pojo.User;
import com.dgutforum.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@Tag(name = "登录相关接口")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    @Operation(summary = "登录")
    public ResVo<String> login(@RequestBody User user){
        log.info("输入的账号和密码：{}",user);

        User e = userService.login(user);

        if(e!=null){
            Map<String,Object> claims = new HashMap<>();
            claims.put("id",e.getId());
            claims.put("username",e.getUsername());

            String jwt = JwtUtils.generateJwt(claims);
            return ResVo.ok(jwt);
        }
        else return ResVo.ok(null);
    }
}

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
    public ResVo register(@RequestBody User user) {
        log.info("register infomation: {}", user);


        try {
            userService.register(user);
        } catch (Exception e) {
            return ResVo.ok("用户名重复");
        }

        return ResVo.ok(null);
    }

}

package com.dgutforum.user.controller;

import com.dgutforum.common.result.ResVo;
import com.dgutforum.common.result.Result;
import com.dgutforum.common.result.eunms.StatusEnum;
import com.dgutforum.user.pojo.User;
import com.dgutforum.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "注册相关接口")
public class RegisterController {

    @Autowired
    private UserService userService;

    @Operation(summary = "注册")
    @PostMapping("/get")
    public Result get(@RequestBody User user){
        log.info("要get的id:{}", user.getId());

        User e = userService.get(user);

        return Result.success(e);

    }
}

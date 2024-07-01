package com.dgutforum.user.service.impl;

import com.dgutforum.mapper.UserMapper;
import com.dgutforum.user.pojo.User;
import com.dgutforum.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User login(User user) {
        return userMapper.login(user);
    }

    @Override
    public void register(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        /**
         * username的唯一索引 还没有 抛出异常
          */

        userMapper.register(user);
    }

    @Override
    public User select(User user) {
        return userMapper.select(user);
    }

    @Override
    public void update(User user) {
        user.setUpdateTime(LocalDateTime.now());

        /**
         * username的唯一索引 还没有 抛出异常
         */

        userMapper.update(user);
    }
}

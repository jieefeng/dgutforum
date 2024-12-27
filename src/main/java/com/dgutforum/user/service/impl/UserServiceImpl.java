package com.dgutforum.user.service.impl;

import com.dgutforum.mapper.UserMapper;
import com.dgutforum.user.pojo.*;
import com.dgutforum.user.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User login(User user) {
        return userMapper.login(user);
    }

    @Override
    public Long register(User user) {
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        /**
         * username的唯一索引 还没有 抛出异常
          */

        return userMapper.register(user);
    }

    @Override
    public User select(User user) {
        return userMapper.select(user);
    }

    @Override
    public boolean update(User user) {
        //判断名字唯一
        User ifuser = userMapper.getByUserName(user.getUsername());
        if (ifuser != null && ifuser.getId() != user.getId() ) {
            return false;
        }

        user.setUpdateTime(LocalDateTime.now());

        userMapper.update(user);
        return true;
    }

    @Override
    public void follow_add(Follow follow) {
        follow.setCreateTime(LocalDateTime.now());
        follow.setUpdateTime(LocalDateTime.now());

        userMapper.follow_add(follow);
    }

    @Override
    public void follow_del(Follow follow) {
        userMapper.follow_del(follow);
    }

    public UserVo get(long id) {
        User user = userMapper.get(id);

        UserVo userVo = new UserVo();

//        获取属性
        BeanUtils.copyProperties(user,userVo);

//        计算加入天数
        long enterDaysCount = ChronoUnit.DAYS.between(user.getCreateTime().toLocalDate(), LocalDateTime.now().toLocalDate());
        userVo.setEnterDaysCount(enterDaysCount);

        return userVo;
    }

    @Override
    public List<FollowVo> follower_select(long id) {

        List<Integer> list = userMapper.follower_select(id);

        if(list.size() == 0){
            return null;
        }

        List<FollowVo> followVoList = userMapper.id_select(list);

        return followVoList;
    }

    @Override
    public void registerUserInfo(Long userId) {
        userMapper.registerUserInfo(userId);
    }

    @Override
    public UserInfoVo getUserInfoByUserId(long id) {
        UserInfo userInfo = userMapper.getUserInfoByUserId(id);
        UserInfoVo userInfoVo = new UserInfoVo();
        BeanUtils.copyProperties(userInfo,userInfoVo);

        return userInfoVo;
    }

    @Override
    public List<FollowVo> follow_select(long id) {
        List<Integer> list = userMapper.follow_select(id);

        if(list.size() == 0){
            return null;
        }

        List<FollowVo> followVoList = userMapper.id_select(list);

        return followVoList;
    }


}

package com.dgutforum.user.service;

import com.dgutforum.user.pojo.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User login(User user);

    Long register(User user);

    User select(User user);

    boolean update(User user);

    void follow_add(Follow follow);

    void follow_del(Follow follow);

    UserVo get(long id);

    List<FollowVo> follower_select(long id);

    void registerUserInfo(Long userId);

    UserInfoVo getUserInfoByUserId(long id);

    List<FollowVo> follow_select(long id);
}

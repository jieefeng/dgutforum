package com.dgutforum.user.service;

import com.dgutforum.user.pojo.Follow;
import com.dgutforum.user.pojo.User;
import com.dgutforum.user.pojo.UserVo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    public User login(User user);

    void register(User user);

    User select(User user);

    void update(User user);

    void follow_add(Follow follow);

    void follow_del(Follow follow);


    User get(User user);

    List<UserVo> follow_select(Follow follow);
}

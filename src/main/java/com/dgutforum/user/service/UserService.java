package com.dgutforum.user.service;

import com.dgutforum.user.pojo.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    public User login(User user);

    void register(User user);

    User select(User user);

    void update(User user);
}

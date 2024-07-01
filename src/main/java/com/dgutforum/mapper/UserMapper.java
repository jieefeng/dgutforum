package com.dgutforum.mapper;

import com.dgutforum.user.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {


    @Select("select * from user where username = #{username} and password = #{password}")
    User login(User user);

    @Insert("insert into user(username, password, create_time, update_time) " +
            "values(#{username},#{password},#{createTime},#{updateTime})")
    void register(User user);

    @Select("select * from user where username = #{username}")
    User select(User user);

    void update(User user);
}

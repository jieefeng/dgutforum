package com.dgutforum.mapper;

import com.dgutforum.user.pojo.Follow;
import com.dgutforum.user.pojo.User;
import com.dgutforum.user.pojo.UserVo;
import org.apache.ibatis.annotations.*;

import java.util.List;

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

    @Select("select * from user where id = #{id}")
    User get(User user);

    @Insert("insert into user_relation(user_id, follow_user_id, create_time, update_time) " +
            "values(#{userId},#{followUserId},#{createTime},#{updateTime})")
    void follow_add(Follow follow);

    @Delete("delete from user_relation where user_id = #{userId} and follow_user_id = #{followUserId}")
    void follow_del(Follow follow);


    @Select("select user_id from user_relation where follow_user_id = #{followUserId}")
    List<Integer> follow_select(Follow follow);


    List<UserVo> id_select(List<Integer> list);
}

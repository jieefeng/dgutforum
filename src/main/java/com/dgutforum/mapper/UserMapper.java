package com.dgutforum.mapper;

import com.dgutforum.user.pojo.*;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {


    @Select("select * from user where username = #{username}")
    User login(User user);

    @Insert("insert into user(username, password, create_time, update_time) " +
            "values(#{username},#{password},#{createTime},#{updateTime})")
    @SelectKey(statement = "SELECT LAST_INSERT_ID()", keyProperty = "id", before = false, resultType = Long.class)
    Long register(User user);

    @Select("select * from user where username = #{username}")
    User select(User user);

    void update(User user);

    @Select("select * from user where id = #{id}")
    User get(long id);

    @Select("select * from user where username = #{username}")
    User getByUserName(String username);

    @Insert("insert into user_relation(user_id, follow_user_id, create_time, update_time) " +
            "values(#{userId},#{followUserId},#{createTime},#{updateTime})")
    void follow_add(Follow follow);

    @Delete("delete from user_relation where user_id = #{userId} and follow_user_id = #{followUserId}")
    void follow_del(Follow follow);


    @Select("select follow_user_id from user_relation where user_id = #{id}")
    List<Integer> follow_select(long id);


    List<FollowVo> id_select(List<Integer> list);

    @Insert("insert into user_info(user_id) " +
            "values(#{userId})")
    void registerUserInfo(Long userId);

    @Select("select * from user_info where user_id = #{id}")
    UserInfo getUserInfoByUserId(long id);

    @Select("select user_id from user_relation where follow_user_id = #{id}")
    List<Integer> follower_select(long id);
}

package com.dgutforum.mapper;

import com.dgutforum.article.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserInfoMapper {

    @Update("update user_info " +
            "set prise_count = prise_count + 1 " +
            "where user_id = #{userId} ")
    void incrementPraiseCountByuserId(Long userId);

    @Update("update user_info " +
            "set collection_count = collection_count + 1 " +
            "where user_id = #{userId}")
    void incrementCollectionCountByUserId(Long userId);

    @Update("update user_info " +
            "set read_count = read_count + 1 " +
            "where user_id = #{userId}")
    void incrementReadCountByuserId(Long userId);

    @Update("update user_info " +
            "set publish_count = prise_count + 1 " +
            "where user_id = #{userId}")
    void incrementPublishCount(Long userId);
}

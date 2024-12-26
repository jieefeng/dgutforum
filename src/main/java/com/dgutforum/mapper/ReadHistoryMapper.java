package com.dgutforum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgutforum.article.entity.ReadHistory;
import com.dgutforum.article.vo.BrowseHistoryVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ReadHistoryMapper extends BaseMapper<ReadHistory> {

//    @Insert("insert into read_history(user_id,article_id,read_time) value (#{userId},#{articleId},#{readTime})")
//    void save(ReadHistory readHistory);

    @Select("select * from read_history where user_id = #{userId} and article_id = #{articleId}")
    ReadHistory query(ReadHistory readHistory);

    @Select("select article_id from read_history " +
            "where read_time between #{begin} and #{end} and user_id = #{userId} " +
            "order by read_time desc")
    List<Long> queryBrowseHistory(BrowseHistoryVo browseHistoryVo);
}

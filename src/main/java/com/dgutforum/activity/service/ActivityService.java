package com.dgutforum.activity.service;

import com.dgutforum.activity.vo.ActivityDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ActivityService {

    /**
     * 查询日排行榜
     * @param day
     * @return
     */
    List<ActivityDto> getDayRank(LocalDateTime day);

    /**
     * 查询月排行榜
     * @param month
     * @return
     */
    List<ActivityDto> getMonthRank(LocalDateTime month);

    void cancelPraiseActivity(Long articleId, Long userId, Long commentId);
}

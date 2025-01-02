
package com.dgutforum.activity.service.impl;

import com.dgutforum.activity.eums.StatusEnums;
import com.dgutforum.activity.listener.ActivityListener;
import com.dgutforum.activity.service.ActivityService;
import com.dgutforum.activity.vo.ActivityDto;
import com.dgutforum.activity.vo.ActivityVo;
import com.dgutforum.config.RabbitmqConfig;
import com.dgutforum.mapper.ArticlePraiseMapper;
import com.dgutforum.mapper.CommentPraiseMapper;
import com.dgutforum.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ActivityServiceImpl implements ActivityService {

    private final RabbitTemplate rabbitTemplate;
    private final CommentPraiseMapper commentPraiseMapper;
    private final ArticlePraiseMapper articlePraiseMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final ActivityListener activityListener;
    private final UserMapper userMapper;
    public void addPraiseActivity(Long articleId, Long userId,Long commentId) {
        ActivityVo activityVo = new ActivityVo();
        if(commentId != null){
            activityVo.setCommentId(commentId);
        }
        activityVo.setStatusEnums(StatusEnums.PRAISE);
        activityVo.setArticleId(articleId);
        activityVo.setUserId(userId);
        rabbitTemplate.convertAndSend(RabbitmqConfig.ACTIVITY_DIRECT,RabbitmqConfig.ACTIVITY_BINGING,activityVo);
    }

    public void addReadActivity(Long articleId, Long userId) {
        ActivityVo activityVo = new ActivityVo();
        activityVo.setStatusEnums(StatusEnums.READ);
        activityVo.setArticleId(articleId);
        activityVo.setUserId(userId);
        rabbitTemplate.convertAndSend(RabbitmqConfig.ACTIVITY_DIRECT,RabbitmqConfig.ACTIVITY_BINGING,activityVo);
    }


    @Override
    public List<ActivityDto> getDayRank(LocalDateTime day) {
        String todayKey = activityListener.ACTIVITY_PREFIX + ":" + "day" + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(todayKey, 0, 9);
        //2.转换为list
        List<ActivityDto> activityDtos = new ArrayList<>();
        if (typedTuples != null) {
            for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
                ActivityDto dto = new ActivityDto();
                Long userId = Long.parseLong(tuple.getValue());
                dto.setUserId(userId);  // 假设tuple的value是userId的字符串表示
                dto.setUserName(userMapper.get(userId).getUsername());  // 你可以根据 userId 获取 userName，这里是一个示例方法
                dto.setScore(tuple.getScore().intValue());  // 将score转换为Integer类型
                activityDtos.add(dto);
            }
        }
        return activityDtos;
    }

    @Override
    public List<ActivityDto> getMonthRank(LocalDateTime month) {
        String monthKey = activityListener.ACTIVITY_PREFIX + ":" + "month" + ":" + month.format(DateTimeFormatter.ofPattern("yyyyMM"));
        Set<ZSetOperations.TypedTuple<String>> typedTuples = stringRedisTemplate.opsForZSet().reverseRangeWithScores(monthKey, 0, 9);
        //2.转换为list
        List<ActivityDto> activityDtos = new ArrayList<>();
        if (typedTuples != null) {
            for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
                ActivityDto dto = new ActivityDto();
                long userId = Long.parseLong(tuple.getValue());
                dto.setUserId(userId);  // 假设tuple的value是userId的字符串表示
                dto.setUserName(userMapper.get(userId).getUsername());  // 你可以根据 userId 获取 userName，这里是一个示例方法
                dto.setScore(tuple.getScore().intValue());  // 将score转换为Integer类型
                activityDtos.add(dto);
            }
        }

        return activityDtos;
    }

    @Override
    public void cancelPraiseActivity(Long articleId, Long userId, Long commentId) {
        ActivityVo activityVo = new ActivityVo();
        if(commentId != null){
            activityVo.setCommentId(commentId);
        }
        activityVo.setStatusEnums(StatusEnums.DECREASE_PRAISE);
        activityVo.setArticleId(articleId);
        activityVo.setUserId(userId);
        rabbitTemplate.convertAndSend(RabbitmqConfig.ACTIVITY_DIRECT,RabbitmqConfig.ACTIVITY_BINGING,activityVo);
    }
}




















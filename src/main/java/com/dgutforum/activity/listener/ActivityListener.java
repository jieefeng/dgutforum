package com.dgutforum.activity.listener;

import com.dgutforum.activity.vo.ActivityVo;
import com.dgutforum.config.RabbitmqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ActivityListener {

    //key dgutforum_activity_rank_{user_id}_年月日
    //field:活跃度更新key
    //value:活跃度更新值
    //点赞 + 2分 field:praise/article/{articleId}
    //阅读 + 1分 filed:read/article/{articleId}

    //日活跃榜单
    //key :dgutforum_activity_rank_年月日

    private final RedisTemplate<String,String> redisTemplate;

    private static final String ACTIVITY_PREFIX = "dgutforum_activity_rank";
    private static final String PRAISE_FIELD_PREFIX = "praise/article";

    private static final String READ_FIELD_PREFIX = "read/acticle";

    @RabbitListener(queues = RabbitmqConfig.PRAISE_QUEUE)
    public void addActivity(ActivityVo activityVo){
        switch (activityVo.getStatusEnums()){
            case PRAISE -> {
                String field = PRAISE_FIELD_PREFIX + "/" + activityVo.getArticleId();
                addActivity(activityVo,field,2);
            }
            case READ -> {
                String field = READ_FIELD_PREFIX + "/" + activityVo.getArticleId();
                addActivity(activityVo,field,1);
            }
        }
    }

    private void addActivity(ActivityVo activityVo, String field,Integer score) {
        Long userId = activityVo.getUserId();
        String userActionKey = ACTIVITY_PREFIX
                + "_"
                + userId + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Long value = Long.valueOf((String) redisTemplate.opsForHash().get(userActionKey,field));
        //1.说明之前无加分记录，可以添加
        if(value == null){
            //加分值
            doAddActivity(userActionKey, field, userId, score);
        } else {
            //2，说明之前已经添加 无需增加活跃度
        }
    }

    private void doAddActivity(String userActionKey, String field, Long userId, Integer score) {
        //1.标记用户已经执行过这个活跃度行为
        redisTemplate.opsForHash().put(userActionKey, field,1);
        //2.添加日活跃度
        String todayKey = ACTIVITY_PREFIX + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        redisTemplate.opsForZSet().add(todayKey, String.valueOf(userId), score);
        //3.添加月活跃度
        String monthKey = ACTIVITY_PREFIX + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        redisTemplate.opsForZSet().add(monthKey,String.valueOf(userId), score);
    }
}


























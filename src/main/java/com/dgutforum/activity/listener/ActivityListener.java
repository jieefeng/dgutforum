package com.dgutforum.activity.listener;

import com.dgutforum.activity.vo.ActivityVo;
import com.dgutforum.config.RabbitmqConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class ActivityListener {

    //key dgutforum_activity_rank_{user_id}_年月日
    //field:活跃度更新key
    //value:活跃度更新值
    //给文章点赞 + 2分 field:praise/article/{articleId}
    //给评论点赞 + 2分 field:praise/comment/{commentId}
    //阅读 + 1分 filed:read/article/{articleId}
    //收藏文章: + 3分 field:collection/article/{articleId}5

    //日活跃榜单
    //key :dgutforum_activity_rank_年月日

//    private final RedisTemplate<String,String> redisTemplate;
    private final StringRedisTemplate stringRedisTemplate;

    public static final String ACTIVITY_PREFIX = "dgutforum:activity:rank";
    private static final String PRAISE_ARTICLE_FIELD_PREFIX = "praise/article";
    private static final String PRAISE_COMMENT_FIELD_PREFIX = "praise/comment";
    private static final String READ_FIELD_PREFIX = "read/acticle";

    private static final String COLLECTION_FILED_PREFIX = "collection/article";

    @RabbitListener(queues = RabbitmqConfig.PRAISE_QUEUE)
    public void addActivity(ActivityVo activityVo){
        switch (activityVo.getStatusEnums()){
            case PRAISE -> {
                String field = null;
                if(activityVo.getCommentId() != null){
                    //说明是评论
                    field = PRAISE_COMMENT_FIELD_PREFIX
                            + "/"
                            + activityVo.getCommentId();
                }else {
                    //说明是文章
                    field = PRAISE_ARTICLE_FIELD_PREFIX + "/" + activityVo.getArticleId();
                }
                addActivity(activityVo,field,2);
            }
            case READ -> {
                String field = READ_FIELD_PREFIX + "/" + activityVo.getArticleId();
                addActivity(activityVo,field,1);
            }
            case COLLECTION -> {
                String field = COLLECTION_FILED_PREFIX + "/" + activityVo.getArticleId();
                addActivity(activityVo,field,3);
            }
            case DECREASE_PRAISE -> {
                String field = null;
                if(activityVo.getCommentId() != null){
                    //说明是评论
                    field = PRAISE_COMMENT_FIELD_PREFIX
                            + "/"
                            + activityVo.getCommentId();
                }else {
                    //说明是文章
                    field = PRAISE_ARTICLE_FIELD_PREFIX + "/" + activityVo.getArticleId();
                }
                decreaseActivity(activityVo,field,-2);
            }
            case DECREASE_COLLECTION -> {
                String field = COLLECTION_FILED_PREFIX + "/" + activityVo.getArticleId();
                decreaseActivity(activityVo,field,-3);
            }
        }
    }

    private void decreaseActivity(ActivityVo activityVo, String field,Integer score) {
        Long userId = activityVo.getUserId();
        String userActionKey = ACTIVITY_PREFIX
                + ":"
                + userId + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Object value = stringRedisTemplate.opsForHash().get(userActionKey, field);
        if(value == null){
            //1.如果当天无加分记录 无需减少活跃
        } else {
            //2.说明当天有加分记录 需要减少活跃度
            doDecreaseActivity(userActionKey,field,userId,score);
        }
    }

    private void doDecreaseActivity(String userActionKey, String field, Long userId, Integer score) {
        //1.删除标记用户已经执行过这个活跃度行为
        stringRedisTemplate.opsForHash().delete(userActionKey, field);
        //2.减少添加日活跃度
        String todayKey = ACTIVITY_PREFIX + ":" + "day" + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        stringRedisTemplate.opsForZSet().incrementScore(todayKey, String.valueOf(userId), score);
        //3.减少月活跃度
        String monthKey = ACTIVITY_PREFIX + ":" + "month" + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        stringRedisTemplate.opsForZSet().incrementScore(monthKey,String.valueOf(userId), score);
    }

    private void addActivity(ActivityVo activityVo, String field,Integer score) {
        Long userId = activityVo.getUserId();
        String userActionKey = ACTIVITY_PREFIX
                + ":"
                + userId + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Object value = stringRedisTemplate.opsForHash().get(userActionKey, field);
        if(value == null){
            //1.说明之前无加分记录，可以添加
            //加分值
            doAddActivity(userActionKey, field, userId, score);
        } else {
            //2，说明之前已经添加 无需增加活跃度
        }
    }

    private void doAddActivity(String userActionKey, String field, Long userId, Integer score) {
        //1.标记用户已经执行过这个活跃度行为
        stringRedisTemplate.opsForHash().put(userActionKey, field,"1");
        //2.添加日活跃度
        String todayKey = ACTIVITY_PREFIX + ":" + "day" + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        stringRedisTemplate.opsForZSet().incrementScore(todayKey, String.valueOf(userId), score);
        //3.添加月活跃度
        String monthKey = ACTIVITY_PREFIX + ":" + "month" + ":" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        stringRedisTemplate.opsForZSet().incrementScore(monthKey,String.valueOf(userId), score);
    }
}


























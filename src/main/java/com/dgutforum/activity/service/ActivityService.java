package com.dgutforum.activity.service;

import com.dgutforum.activity.eums.StatusEnums;
import com.dgutforum.activity.vo.ActivityVo;
import com.dgutforum.config.RabbitmqConfig;
import com.dgutforum.mapper.ArticlePraiseMapper;
import com.dgutforum.mapper.CommentPraiseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final RabbitTemplate rabbitTemplate;
    private final CommentPraiseMapper commentPraiseMapper;
    private final ArticlePraiseMapper articlePraiseMapper;
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
        rabbitTemplate.convertAndSend(RabbitmqConfig.ACTIVITY_DIRECT,RabbitmqConfig.ACTIVITY_BINGING);
    }
}

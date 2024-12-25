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
    public void praise(ActivityVo activityVo) {
        //判断是给文章还是评论点赞
        //1.是文章
        if(activityVo.getArticleId() != null){
            //1.1保存点赞
            articlePraiseMapper.save(activityVo.getArticleId(),activityVo.getUserId());
            activityVo.setStatusEnums(StatusEnums.PRAISE);
        } else {
            //1.2是评论
            commentPraiseMapper.save(activityVo);
            activityVo.setStatusEnums(StatusEnums.COMMENT);
        }
        //2.增加活跃度
        rabbitTemplate.convertAndSend(RabbitmqConfig.ACTIVITY_DIRECT,RabbitmqConfig.ACTIVITY_BINGING,activityVo);
    }
}

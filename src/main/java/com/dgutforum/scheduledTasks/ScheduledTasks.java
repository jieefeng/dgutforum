package com.dgutforum.scheduledTasks;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private final RedisTemplate<String,String> redisTemplate;

    // 每天晚上 11 点执行
    @Scheduled(cron = "0 0 23 * * ?")
    public void runDailyTask() {
        System.out.println("每天晚上 11 点执行的任务");
        //TODO 设置下一天的日活跃榜单的持续时间 这里放置你每天要执行的任务逻辑
    }

    // 每月最后一天晚上 11 点执行
    @Scheduled(cron = "0 0 23 L * ?")
    public void runLastDayOfMonthTask() {
        System.out.println("每月最后一天晚上 11 点执行的任务");
        //TODO 设置下一个月的月活跃榜单的持续时间 这里放置你每月最后一天要执行的任务逻辑
    }
}

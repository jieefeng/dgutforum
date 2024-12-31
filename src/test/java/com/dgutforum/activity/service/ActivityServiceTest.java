package com.dgutforum.activity.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;


@SpringBootTest  // 启动 Spring Boot 测试上下文
class ActivityServiceTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    void getDayRank() {
        Long size = stringRedisTemplate.opsForZSet().size("dgutforum:activity:rank:day:20241226");
        System.out.printf("zset长度:{}", size);
    }
}
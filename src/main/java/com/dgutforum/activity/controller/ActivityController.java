package com.dgutforum.activity.controller;

import com.dgutforum.activity.service.ActivityService;
import com.dgutforum.activity.vo.ActivityDto;
import com.dgutforum.common.result.Result;
import com.dgutforum.mapper.ArticlePraiseMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/activity")
@Tag(name = "活跃度相关接口")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;


    /**
     * 获取日排行榜
     * @param day
     * @return
     */
    @GetMapping("/getDayRank/{day}")
    public Result getDayRank(@PathVariable("day") String day) {
        LocalDateTime date = LocalDateTime.parse(day + "T00:00:00"); // 假设传递的日期格式是 "yyyy-MM-dd"
        List<ActivityDto> activityDtoList = activityService.getDayRank(date);
        return Result.success(activityDtoList);
    }

    /**
     * 获取月排行榜
     * @param month
     * @return
     */
    @GetMapping("/getMonthRank/{month}")
    public Result getMonthRank(@PathVariable("month") String month) {
        LocalDateTime date = LocalDateTime.parse(month + "-01T00:00:00"); // 假设传递的月份格式是 "yyyy-MM"
        List<ActivityDto> activityDtoList = activityService.getMonthRank(date);
        return Result.success(activityDtoList);
    }
}

package com.dgutforum.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

    private Long id;
    private Long userId;
    private Long followUserId; //关注的人
    private LocalDateTime createTime; //创建时间
    private LocalDateTime updateTime;
}

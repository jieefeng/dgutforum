package com.dgutforum.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Follow {

    private int id;
    private int userId;
    private int followUserId;
    private LocalDateTime createTime; //创建时间
    private LocalDateTime updateTime;
}

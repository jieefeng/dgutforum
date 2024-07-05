package com.dgutforum.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private int id;
    private String username;
    private String password;
    private String photo;
    private String profile;
    private LocalDateTime createTime; //创建时间
    private LocalDateTime updateTime; //修改时间

}
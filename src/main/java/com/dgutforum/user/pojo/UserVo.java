package com.dgutforum.user.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo  {

    private String username;
    private String photo;
    private String profile;
    private String email;
    private long followerCount;
    private long followCount;
    private long enterDaysCount;
}

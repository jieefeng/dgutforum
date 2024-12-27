package com.dgutforum.user.pojo;

import lombok.Data;

@Data
public class UserInfo {
    private long id;
    private long userId;
    private long priseCount;
    private long collectionCount;
    private long readCount;
    private long publishCount;

}

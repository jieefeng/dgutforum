package com.dgutforum.common.exception;


import com.dgutforum.common.result.eunms.StatusEnum;

public class ExceptionUtil {

    public static ForumException of(StatusEnum status, Object... args) {
        return new ForumException(status, args);
    }

}

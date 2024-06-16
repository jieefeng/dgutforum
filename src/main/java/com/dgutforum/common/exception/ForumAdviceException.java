package com.dgutforum.common.exception;


import com.dgutforum.common.result.Status;
import com.dgutforum.common.result.eunms.StatusEnum;
import lombok.Getter;

/**
 * 业务异常
 */
public class ForumAdviceException extends RuntimeException {
    @Getter
    private Status status;

    public ForumAdviceException(Status status) {
        this.status = status;
    }

    public ForumAdviceException(int code, String msg) {
        this.status = Status.newStatus(code, msg);
    }

    public ForumAdviceException(StatusEnum statusEnum, Object... args) {
        this.status = Status.newStatus(statusEnum, args);
    }

}

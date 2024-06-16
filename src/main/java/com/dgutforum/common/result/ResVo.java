package com.dgutforum.common.result;


import com.dgutforum.common.result.eunms.StatusEnum;
import lombok.Data;

import java.io.Serializable;

@Data
public class ResVo<T> implements Serializable {
    private static final long serialVersionUID = -510306209659393854L;
//    @ApiModelProperty(value = "返回结果说明", required = true)
    private Status status;

//    @ApiModelProperty(value = "返回的实体结果", required = true)
    private T result;


    public ResVo() {
    }

    public ResVo(Status status) {
        this.status = status;
    }

    public ResVo(T t) {
        status = Status.newStatus(StatusEnum.SUCCESS);
        this.result = t;
    }

    public static <T> ResVo<T> ok(T t) {
        return new ResVo<T>(t);
    }

    @SuppressWarnings("unchecked")
    public static <T> ResVo<T> fail(StatusEnum status, Object... args) {
        return new ResVo<>(Status.newStatus(status, args));
    }

    public static <T> ResVo<T> fail(Status status) {
        return new ResVo<>(status);
    }
}
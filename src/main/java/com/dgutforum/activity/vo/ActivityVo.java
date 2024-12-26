package com.dgutforum.activity.vo;

import com.dgutforum.activity.eums.StatusEnums;
import lombok.Data;

import java.io.Serializable;

@Data
public class ActivityVo implements Serializable {
    private StatusEnums statusEnums;
    private Long userId;
    private Long articleId;
    private Long commentId;
}

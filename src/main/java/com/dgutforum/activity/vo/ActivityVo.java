package com.dgutforum.activity.vo;

import com.dgutforum.activity.eums.StatusEnums;
import lombok.Data;

@Data
public class ActivityVo {
    private StatusEnums statusEnums;
    private Long userId;
    private Long articleId;
    private Long commentId;
}

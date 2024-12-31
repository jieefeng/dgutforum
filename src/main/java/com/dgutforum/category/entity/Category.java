package com.dgutforum.category.entity;

import com.dgutforum.common.dto.BaseDO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Category {
    private Long id;
    private String categoryName;
    private Short rank;
    private Short deleted;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}

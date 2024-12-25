package com.dgutforum.article.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BrowseHistoryVo {
    private Long userId;
    private LocalDateTime begin;
    private LocalDateTime end;
}

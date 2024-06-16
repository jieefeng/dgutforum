package com.dgutforum.comment.req;

import lombok.Data;

@Data
public class CommentListReq {

    private Long articleId;

    private int PageNumber; //页码

    private int PageSsize; //每页条数

}

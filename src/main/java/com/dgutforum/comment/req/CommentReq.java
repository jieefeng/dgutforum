package com.dgutforum.comment.req;


import lombok.Data;

@Data
public class CommentReq {

    private Long id;
    private Long articleId;
    private Long userId;
    private String content;
    private Long parise;
    private short deleted;
}

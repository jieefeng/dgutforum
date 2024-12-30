package com.dgutforum.comment.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgutforum.comment.dto.CommentDto;
import com.dgutforum.comment.vo.CommentChildVo;
import com.dgutforum.comment.vo.CommentVo;
import com.dgutforum.mapper.CommentUserInfoMapper;
import com.dgutforum.comment.entity.Comment;
import com.dgutforum.mapper.CommentMapper;
import com.dgutforum.comment.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentUserInfoMapper commentUserInfoMapper;


    @Override
    public List<CommentVo> list(Long articleId) {
        //1.根据文章id查询文章的父评论 按照评论更新时间排序
        List<CommentVo> list = commentUserInfoMapper.queryTopCommentByArticleId(articleId);
//        List<CommentVo> commentVoList = new ArrayList<>(list.size());
//        //2.根据文章id和父评论id查询所有子评论   并且根据更新时间排序
//        for(CommentDto commentDto : list){
//            CommentVo commentVo = new CommentVo();
//            BeanUtil.copyProperties(commentVo,commentDto);
//        }
        //2.筛选所有父评论
        List<CommentChildVo> commentChildVos = new ArrayList<>();
        for (CommentVo commentVo : list){
            List<CommentChildVo> commentChildVoList = commentUserInfoMapper.queryChildrenComment(commentVo.getArticleId(), commentVo.getId());
            commentVo.setCommentChildVoList(commentChildVoList);
        }
        return list;
    }

//    /**
//     * 根据文章id分页查询评价
//     * @param commentListReq
//     * @return
//     */
//    public List<CommentDto> listTen(CommentListReq commentListReq){
//        PageHelper.startPage(commentListReq.getPageNumber(),commentListReq.getPageSsize());
//        Page<CommentDto> commentDtoPage = commentUserInfoMapper.list(commentListReq.getArticleId());
//        long total = commentDtoPage.getTotal();
//        List<CommentDto> result = commentDtoPage.getResult();
//        return result;
//    }
}

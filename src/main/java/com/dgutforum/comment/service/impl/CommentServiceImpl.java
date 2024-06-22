package com.dgutforum.comment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgutforum.comment.dto.CommentDto;
import com.dgutforum.comment.vo.CommentVo;
import com.dgutforum.mapper.CommentUserInfoMapper;
import com.dgutforum.comment.req.CommentListReq;
import com.dgutforum.common.result.ResVo;
import com.dgutforum.comment.entity.Comment;
import com.dgutforum.mapper.CommentMapper;
import com.dgutforum.comment.service.CommentService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    private CommentUserInfoMapper commentUserInfoMapper;


    @Override
    public ResVo<List<CommentVo>> list(CommentListReq commentListReq) {
//        分页查询
//        //1.根据文章id查询评价 每次查询十个
//        List<CommentDto> list = listTen(commentListReq);
        //1.根据文章id查询评价 查询全部 按照评论更新时间排序
        List<CommentDto> list = commentUserInfoMapper.queryCommentByArticleId(commentListReq.getArticleId());
        List<CommentVo> commentVoList = new ArrayList<>(list.size());
        //2.根据文章id和父评论id查询所有子评论   并且根据更新时间排序
        for(CommentDto commentDto : list){
            CommentVo commentVo = new CommentVo();
            BeanUtil.copyProperties(commentDto,commentVo);
            commentVo.setChildren(commentUserInfoMapper.queryChildrenComment(commentDto.getArticleId(),commentDto.getId()));
        }
        return ResVo.ok(commentVoList);
    }

    /**
     * 根据文章id分页查询评价
     * @param commentListReq
     * @return
     */
    public List<CommentDto> listTen(CommentListReq commentListReq){
        PageHelper.startPage(commentListReq.getPageNumber(),commentListReq.getPageSsize());
        Page<CommentDto> commentDtoPage = commentUserInfoMapper.list(commentListReq.getArticleId());
        long total = commentDtoPage.getTotal();
        List<CommentDto> result = commentDtoPage.getResult();
        return result;
    }
}

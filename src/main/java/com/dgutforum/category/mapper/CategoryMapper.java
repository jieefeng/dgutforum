package com.dgutforum.category.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dgutforum.category.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

    @Select("select * from category")
    List<Category> queryAll();
}

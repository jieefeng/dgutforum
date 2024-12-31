package com.dgutforum.category.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dgutforum.category.entity.Category;
import com.dgutforum.category.mapper.CategoryMapper;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl  extends ServiceImpl<CategoryMapper, Category> implements CategoryService {


}

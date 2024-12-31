package com.dgutforum.category.controller;

import com.dgutforum.category.entity.Category;
import com.dgutforum.category.mapper.CategoryMapper;
import com.dgutforum.category.service.CategoryService;
import com.dgutforum.common.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
@Tag(name = "分类相关接口")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    /**
     * 获取所有分类
     * @return
     */
    @GetMapping("/getAll")
    @Operation(summary = "查询全部分类")
    public Result getAllCategory(){
        List<Category> list = categoryMapper.queryAll();
        return Result.success(list);
    }
}






























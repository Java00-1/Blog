package com.pdf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pdf.domain.entity.Category;
import com.pdf.utils.ResponseResult;


/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2022-09-27 08:59:54
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}


package com.pdf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdf.constant.SystemConstants;
import com.pdf.domain.entity.Article;
import com.pdf.domain.entity.Category;
import com.pdf.domain.vo.CategoryVo;
import com.pdf.mapper.CategoryMapper;
import com.pdf.service.ArticleService;
import com.pdf.service.CategoryService;
import com.pdf.utils.BeanCopyUtils;
import com.pdf.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2022-09-27 08:59:54
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //select article table which status is published
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);
        //select article category id and distinct
        Set<Long> categoryId = articleList.stream()
                .map(new Function<Article, Long>() {
                    @Override
                    public Long apply(Article article) {
                        return article.getCategoryId();
                    }
                })
                .collect(Collectors.toSet());
        //select category table
        //调用CategoryMapper封装的方法进行查询
        List<Category> categories = listByIds(categoryId);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        //package vo
        return ResponseResult.okResult(categoryVos);
    }
}


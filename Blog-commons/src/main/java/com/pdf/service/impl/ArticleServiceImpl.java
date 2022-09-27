package com.pdf.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pdf.constant.SystemConstants;
import com.pdf.domain.entity.Article;
import com.pdf.domain.entity.Category;
import com.pdf.domain.vo.ArticleDetailVo;
import com.pdf.domain.vo.ArticleListVo;
import com.pdf.domain.vo.HotArticleVo;
import com.pdf.domain.vo.PageVo;
import com.pdf.mapper.ArticleMapper;
import com.pdf.service.ArticleService;
import com.pdf.service.CategoryService;
import com.pdf.utils.BeanCopyUtils;
import com.pdf.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 文章表(Article)表服务实现类
 *
 * @author makejava
 * @since 2022-09-27 07:59:45
 */
@Service("articleService")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
        //最多查询10条 分页查询
        Page<Article> page = new Page<>(1,10);
        page(page, queryWrapper);
        //bean copy
        List<Article> records = page.getRecords();
        List<HotArticleVo> hotArticleVos = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);
        //封装进行返回
        return ResponseResult.okResult(hotArticleVos);
    }

    @Override
    /**
     * select article detail
     */
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //category exist select
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        //eq方法的重写
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>0,Article::getCategoryId,categoryId);
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        //oder by desc by isTop
        queryWrapper.orderByDesc(Article::getIsTop);
        //pagination select
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();

        //select categoryName

        //articleId去查询articleName进行设置
        /*for (Article article : articles) {
            Category category = categoryService.getById(article.getCategoryId());
            article.setCategoryName(category.getName());
        }*/
        articles.stream()
                .map(new Function<Article, Article>() {
                    @Override
                    public Article apply(Article article) {
                        return article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
                        //return article;
                    }
                })
                .collect(Collectors.toList());

        //封装查询结果
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //select article by id
        Article article = getById(id);
        //convert to vo
        ArticleDetailVo detailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //select categoryName by categoryId
        Category category = categoryService.getById(article.getCategoryId());
        if (category != null){
            detailVo.setCategoryName(category.getName());
        }
        //package
        return ResponseResult.okResult(detailVo);
    }
}


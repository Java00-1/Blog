package com.pdf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pdf.domain.entity.Article;
import com.pdf.utils.ResponseResult;


/**
 * 文章表(Article)表服务接口
 *
 * @author makejava
 * @since 2022-09-27 07:59:45
 */
public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);
}


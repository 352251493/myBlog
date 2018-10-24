package com.gxg.service;

import com.gxg.entities.Article;

import java.util.List;

/**
 * 提供文章相关的业务处理接口
 * @author 郭欣光
 * @date 2018/10/24 15:48
 */
public interface ArticleService {

    /**
     * 获得最近几次的文章列表
     * @return 文章列表
     * @author 郭欣光
     */
    public List<Article> getAllArticleLately();
}

package com.gxg.dao;

import com.gxg.entities.Article;

import java.util.List;

/**
 * @author 郭欣光
 * @date 2018/10/24 16:07
 */
public interface ArticleDao {
    /**
     * 获得文章数量
     * @return 文章数量
     * @author 郭欣光
     */
    public int getCount();

    /**
     * 获取指定范围的按照修改时间降序排序排列的文章列表
     * @param start 开始坐标
     * @param length 获取长度
     * @return 文章列表
     * @author 郭欣光
     */
    public List<Article> getArticleByLimitOrderByModificationTime(int start, int length);
}
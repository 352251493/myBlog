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

    /**
     * 根据id获得文章数量
     * @param id 文章id
     * @return 文章数量
     * @author 郭欣光
     */
    public int getCountById(String id);

    /**
     * 创建文章
     * @param article 文章信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int createArticle(Article article);

    /**
     * 根据label获取文章个数
     * @param label 标签
     * @return 文章个数
     * @author 郭欣光
     */
    public int getCountByLabel(String label);

    /**
     * 根据文章标签和起始位置获得按照修改时间排序后指定个数的文章列表
     * @param label 文章标签
     * @param start 起始位置
     * @param length 文章个数
     * @return 文章列表
     * @author 郭欣光
     */
    public List<Article> getArticleByLabelAndLLimitOrderByModificationTime(String label, int start, int length);
}

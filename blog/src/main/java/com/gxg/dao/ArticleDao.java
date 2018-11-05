package com.gxg.dao;

import com.gxg.entities.Article;

import java.sql.Timestamp;
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

    /**
     * 根据id获取文章信息
     * @param id 文章id
     * @return 文章信息
     * @author 郭欣光
     */
    public Article getArticleById(String id);

    /**
     * 获取指定文章标签大于某一修改时间的文章数量
     * @param modificationTime 修改时间
     * @param label 文章标签
     * @return 文章数量
     * @author 郭欣光
     */
    public int getCountByLabelAndMoreThanTheModificationTime(Timestamp modificationTime, String label);

    /**
     * 获得指定标签刚刚大于指定修改时间的文章
     * @param modificationTime 修改时间
     * @param label 文章标签
     * @return 文章信息
     * @author 郭欣光
     */
    public Article getArticleByLabelAndJustMoreThanTheModificationTime(Timestamp modificationTime, String label);

    /**
     * 获取指定标签小于某一修改时间的文章数量
     * @param modificationTime 修改时间
     * @param label 指定标签
     * @return 文章数量
     * @author 郭欣光
     */
    public int getCountByLabelAndLessThanTheModificationTime(Timestamp modificationTime, String label);

    /**
     * 获得指定标签刚刚小于指定修改时间的文章
     * @param modificationTime 修改时间
     * @param label 指定标签
     * @return 文章信息
     * @author 郭欣光
     */
    public Article getArticleByLabelAndJustLessThanTheModificationTime(Timestamp modificationTime, String label);

    /**
     * 更新文章数据库信息
     * @param article 文章信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int updateArticle(Article article);

    /**
     * 获取指定标签按阅读数量排序的指定区间的文章信息
     * @param label 标签
     * @param start 区间开始
     * @param length 获取个数
     * @return 文章信息
     * @author 郭欣光
     */
    public List<Article> getArticleByLabelAndLimitOrderByReadNumber(String label, int start, int length);

    /**
     * 删除文章
     * @param article 文章信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int deleteArticle(Article article);

    /**
     * 获取按照阅读数量排序的指定范围的文章信息
     * @param start 开始位置
     * @param length 获取个数
     * @return 文章信息
     * @author 郭欣光
     */
    public List<Article> getArticleByLimitOrderByReadNumber(int start, int length);
}

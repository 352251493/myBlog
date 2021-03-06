package com.gxg.dao;

import com.gxg.entities.ArticleComment;

import java.util.List;

/**
 * 文章评论与数据库相关接口
 * @author 郭欣光
 * @date 2018/11/1 10:54
 */
public interface ArticleCommentDao {

    /**
     * 根据文章ID获取该文章评论个数
     * @param articleId 文章id
     * @return 该文章评论个数
     * @author 郭欣光
     */
    public int getCountByArticleId(String articleId);

    /**
     * 根据文章ID按照时间顺序获取一定范围内的文章评论
     * @param articleId 文章ID
     * @param start 开始位置
     * @param length 获得数量
     * @return 文章评论列表
     * @author 郭欣光
     */
    public List<ArticleComment> getArticleCommentByArticleIdAndLimitOrderByCreateTime(String articleId, int start, int length);

    /**
     * 根据id获得文章评论个数
     * @param id 文章评论ID
     * @return 文章评论个数
     * @author 郭欣光
     */
    public int getCountById(String id);

    /**
     * 添加文章评论
     * @param articleComment 文章评论信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int createArticleComment(ArticleComment articleComment);

    /**
     * 删除文章评论
     * @param articleCommentId 文章评论ID
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int deleteArticleComment(String articleCommentId);
}

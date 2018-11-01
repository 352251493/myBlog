package com.gxg.dao.impl;

import com.gxg.dao.ArticleCommentDao;
import com.gxg.dao.rowmapper.ArticleCommentMapper;
import com.gxg.entities.ArticleComment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 郭欣光
 * @date 2018/11/1 10:55
 */
@Repository(value = "articleCommentDao")
public class ArticleCommentDaoImpl implements ArticleCommentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据文章ID获取该文章评论个数
     *
     * @param articleId 文章id
     * @return 该文章评论个数
     * @author 郭欣光
     */
    @Override
    public int getCountByArticleId(String articleId) {
        String sql = "select count(1) from article_comment where article_id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, articleId);
        return rowCount;
    }

    /**
     * 根据文章ID按照时间顺序获取一定范围内的文章评论
     *
     * @param articleId 文章ID
     * @param start     开始位置
     * @param length    获得数量
     * @return 文章评论列表
     * @author 郭欣光
     */
    @Override
    public List<ArticleComment> getArticleCommentByArticleIdAndLimitOrderByCreateTime(String articleId, int start, int length) {
        String sql = "select * from article_comment where article_id=? order by create_time limit ?, ?";
        List<ArticleComment> articleCommentList = jdbcTemplate.query(sql, new ArticleCommentMapper(), articleId, start, length);
        return articleCommentList;
    }
}

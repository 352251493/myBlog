package com.gxg.dao.rowmapper;

import com.gxg.entities.ArticleComment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2018/11/1 11:17
 */
public class ArticleCommentMapper implements RowMapper<ArticleComment> {

    @Override
    public ArticleComment mapRow(ResultSet resultSet, int i) throws SQLException {
        ArticleComment articleComment = new ArticleComment();
        articleComment.setId(resultSet.getString("id"));
        articleComment.setHeadImg(resultSet.getString("head_img"));
        articleComment.setComment(resultSet.getString("comment"));
        articleComment.setName(resultSet.getString("name"));
        articleComment.setEmail(resultSet.getString("email"));
        articleComment.setArticleId(resultSet.getString("article_id"));
        articleComment.setCreateTime(resultSet.getTimestamp("create_time"));
        return articleComment;
    }
}

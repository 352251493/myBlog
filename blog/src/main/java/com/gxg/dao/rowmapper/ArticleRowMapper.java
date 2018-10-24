package com.gxg.dao.rowmapper;

import com.gxg.entities.Article;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2018/10/24 16:38
 */
public class ArticleRowMapper implements RowMapper<Article> {

    @Override
    public Article mapRow(ResultSet resultSet, int i) throws SQLException {
        Article article = new Article();
        article.setId(resultSet.getString("id"));
        article.setTitle(resultSet.getString("title"));
        article.setArticleAbstract(resultSet.getString("abstract"));
        article.setLabel(resultSet.getString("label"));
        article.setImgUrl(resultSet.getString("img_url"));
        article.setArticleUrl(resultSet.getString("article_url"));
        article.setReadNumber(resultSet.getInt("read_number"));
        article.setCreateTime(resultSet.getTimestamp("create_time"));
        article.setModificationTime(resultSet.getTimestamp("modification_time"));
        article.setAuthor(resultSet.getString("author"));
        return article;
    }
}

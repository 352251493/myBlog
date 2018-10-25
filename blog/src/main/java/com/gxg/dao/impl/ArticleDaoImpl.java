package com.gxg.dao.impl;

import com.gxg.dao.ArticleDao;
import com.gxg.dao.rowmapper.ArticleRowMapper;
import com.gxg.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 郭欣光
 * @date 2018/10/24 16:08
 */

@Repository(value = "articleDao")
public class ArticleDaoImpl implements ArticleDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获得文章数量
     *
     * @return 文章数量
     * @author 郭欣光
     */
    @Override
    public int getCount() {
        String sql = "select count(1) from article";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    /**
     * 获取指定范围的按照修改时间降序排序排列的文章列表
     *
     * @param start  开始坐标
     * @param length 获取长度
     * @return 文章列表
     * @author 郭欣光
     */
    @Override
    public List<Article> getArticleByLimitOrderByModificationTime(int start, int length) {
        String sql = "select * from article order by modification_time desc limit ?, ?";
        List<Article> articleList = jdbcTemplate.query(sql, new ArticleRowMapper(), start, length);
        return articleList;
    }

    /**
     * 根据id获得文章数量
     *
     * @param id 文章id
     * @return 文章数量
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from article where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 创建文章
     *
     * @param article 文章信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int createArticle(Article article) {
        String sql = "insert into article(id, title, abstract, label, img_url, article_url, read_number, create_time, modification_time, author) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, article.getId(), article.getTitle(), article.getArticleAbstract(), article.getLabel(), article.getImgUrl(), article.getArticleUrl(), article.getReadNumber(), article.getCreateTime(), article.getModificationTime(), article.getAuthor());
        return changeCount;
    }
}

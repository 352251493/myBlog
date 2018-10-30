package com.gxg.dao.impl;

import com.gxg.dao.ArticleDao;
import com.gxg.dao.rowmapper.ArticleRowMapper;
import com.gxg.entities.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    /**
     * 根据label获取文章个数
     *
     * @param label 标签
     * @return 文章个数
     * @author 郭欣光
     */
    @Override
    public int getCountByLabel(String label) {
        String sql = "select count(1) from article where label=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, label);
        return rowCount;
    }

    /**
     * 根据文章标签和起始位置获得按照修改时间排序后指定个数的文章列表
     *
     * @param label  文章标签
     * @param start  起始位置
     * @param length 文章个数
     * @return 文章列表
     * @author 郭欣光
     */
    @Override
    public List<Article> getArticleByLabelAndLLimitOrderByModificationTime(String label, int start, int length) {
        String sql = "select * from article where label=? order by modification_time desc limit ?, ?";
        List<Article> articleList = jdbcTemplate.query(sql, new ArticleRowMapper(), label, start, length);
        return articleList;
    }

    /**
     * 根据id获取文章信息
     *
     * @param id 文章id
     * @return 文章信息
     * @author 郭欣光
     */
    @Override
    public Article getArticleById(String id) {
        String sql = "select * from article where id=?";
        Article article = jdbcTemplate.queryForObject(sql, new ArticleRowMapper(), id);
        return article;
    }

    /**
     * 获取指定文章标签大于某一修改时间的文章数量
     * @param modificationTime 修改时间
     * @param label 文章标签
     * @return 文章数量
     * @author 郭欣光
     */
    @Override
    public int getCountByLabelAndMoreThanTheModificationTime(Timestamp modificationTime, String label) {
        String sql = "select count(1) from article where modification_time>? and label=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, modificationTime, label);
        return rowCount;
    }

    /**
     * 获得指定标签刚刚大于指定修改时间的文章
     * @param modificationTime 修改时间
     * @param label 文章标签
     * @return 文章信息
     * @author 郭欣光
     */
    @Override
    public Article getArticleByLabelAndJustMoreThanTheModificationTime(Timestamp modificationTime, String label) {
        String sql = "select * from article where modification_time>? and label=? order by modification_time limit 0, 1";
        Article article = jdbcTemplate.queryForObject(sql, new ArticleRowMapper(), modificationTime, label);
        return article;
    }

    /**
     * 获取指定标签小于某一修改时间的文章数量
     * @param modificationTime 修改时间
     * @param label 指定标签
     * @return 文章数量
     * @author 郭欣光
     */
    @Override
    public int getCountByLabelAndLessThanTheModificationTime(Timestamp modificationTime, String label) {
        String sql = "select count(1) from article where modification_time<? and label=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, modificationTime, label);
        return rowCount;
    }

    /**
     * 获得指定标签刚刚小于指定修改时间的文章
     * @param modificationTime 修改时间
     * @param label 指定标签
     * @return 文章信息
     * @author 郭欣光
     */
    @Override
    public Article getArticleByLabelAndJustLessThanTheModificationTime(Timestamp modificationTime, String label) {
        String sql = "select * from article where modification_time<? and label=? order by modification_time desc limit 0, 1";
        Article article = jdbcTemplate.queryForObject(sql, new ArticleRowMapper(), modificationTime, label);
        return article;
    }

    /**
     * 更新文章数据库信息
     *
     * @param article 文章信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int updateArticle(Article article) {
        String sql = "update article set title=?, abstract=?, label=?, img_url=?, article_url=?, read_number=?, create_time=?, modification_time=?, author=? where id=?";
        int changeCount = jdbcTemplate.update(sql, article.getTitle(), article.getArticleAbstract(), article.getLabel(), article.getImgUrl(), article.getArticleUrl(), article.getReadNumber(), article.getCreateTime(), article.getModificationTime(), article.getAuthor(), article.getId());
        return changeCount;
    }

    /**
     * 获取指定标签按阅读数量排序的指定区间的文章信息
     *
     * @param label  标签
     * @param start  区间开始
     * @param length 获取个数
     * @return 文章信息
     * @author 郭欣光
     */
    @Override
    public List<Article> getArticleByLabelAndLimitOrderByReadNumber(String label, int start, int length) {
        String sql = "select * from article where label=? order by read_number desc, modification_time desc limit ?, ?";
        List<Article> articleList = jdbcTemplate.query(sql, new ArticleRowMapper(), label, start, length);
        return articleList;
    }

    /**
     * 删除文章
     *
     * @param article 文章信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteArticle(Article article) {
        String sql = "delete from article where id=?";
        int changeCount = jdbcTemplate.update(sql, article.getId());
        return changeCount;
    }
}

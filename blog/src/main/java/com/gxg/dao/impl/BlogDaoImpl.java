package com.gxg.dao.impl;

import com.gxg.dao.BlogDao;
import com.gxg.entities.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * 博客信息与数据交互
 * @author 郭欣光
 * @date 2018/10/15 15:14
 */

@Repository(value = "blogDao")
public class BlogDaoImpl implements BlogDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 博客信息数目
     *
     * @return 博客信息数目
     * @author 郭欣光
     */
    @Override
    public int getBlogNumber() {
        String sql = "select count(1) from blog";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    /**
     * 获取博客信息
     *
     * @return 博客信息
     * @author 郭欣光
     */
    @Override
    public Blog getBlog() {
        String sql = "select * from blog limit 0, 1";

        return null;
    }
}

package com.gxg.dao.impl;

import com.gxg.dao.BlogDao;
import com.gxg.dao.rowmapper.BlogRowMapper;
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
        Blog blog = jdbcTemplate.queryForObject(sql, new BlogRowMapper());
        return blog;
    }

    /**
     * 新增博客信息
     *
     * @param blog 博客信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int insertBlog(Blog blog) {
        String sql = "insert into blog(owner_name, owner_introduction, logo_ico, logo, owner_head_img, owner_qq_img, owner_weibo_img, owner_weixin_img, owner_github, owner_email) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, blog.getOwnerName(), blog.getOwnerIntroduction(), blog.getLogoIco(), blog.getLogo(), blog.getOwnerHeadImg(), blog.getOwnerQqImg(), blog.getOwnerWeiboImg(), blog.getOwnerWeixinImg(), blog.getOwnerGithub(), blog.getOwnerEmail());
        return changeCount;
    }

    /**
     * 根据原ownerName修改博客信息
     *
     * @param blog      博客信息
     * @param ownerName 原博客所属者昵称
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int updateBlogByOwnerName(Blog blog, String ownerName) {
        String sql = "update blog set owner_name=?, owner_introduction=?, logo_ico=?, logo=?, owner_head_img=?, owner_qq_img=?, owner_weibo_img=?, owner_weixin_img=?, owner_github=?, owner_email=? where owner_name=?";
        int changeCount = jdbcTemplate.update(sql, blog.getOwnerName(), blog.getOwnerIntroduction(), blog.getLogoIco(), blog.getLogo(), blog.getOwnerHeadImg(), blog.getOwnerQqImg(), blog.getOwnerWeiboImg(), blog.getOwnerWeixinImg(), blog.getOwnerGithub(), blog.getOwnerEmail(), ownerName);
        return changeCount;
    }
}

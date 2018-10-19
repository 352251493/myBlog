package com.gxg.dao;

import com.gxg.entities.Blog;

/**
 * 博客信息与数据库交互接口
 * @author 郭欣光
 * @date 2018/10/15 15:12
 */
public interface BlogDao {

    /**
     * 博客信息数目
     * @return 博客信息数目
     * @author 郭欣光
     */
    public int getBlogNumber();

    /**
     * 获取博客信息
     * @return 博客信息
     * @author 郭欣光
     */
    public Blog getBlog();

    /**
     * 新增博客信息
     * @param blog 博客信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int insertBlog(Blog blog);

    /**
     * 根据原ownerName修改博客信息
     * @param blog 博客信息
     * @param ownerName 原博客所属者昵称
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int updateBlogByOwnerName(Blog blog, String ownerName);
}

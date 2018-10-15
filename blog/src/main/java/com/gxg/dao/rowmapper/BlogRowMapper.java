package com.gxg.dao.rowmapper;

import com.gxg.entities.Blog;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2018/10/15 15:19
 */
public class BlogRowMapper implements RowMapper<Blog> {

    @Override
    public Blog mapRow(ResultSet resultSet, int i) throws SQLException {
        Blog blog = new Blog();
        blog.setOwnerName(resultSet.getString("owner_name"));
        blog.setOwnerIntroduction(resultSet.getString("owner_introduction"));
        blog.setLogoIco(resultSet.getString("logo_ico"));
        blog.setLogo(resultSet.getString("logo"));
        blog.setOwnerHeadImg(resultSet.getString("owner_head_img"));
        blog.setOwnerQqImg(resultSet.getString("owner_qq_img"));
        blog.setOwnerWeiboImg(resultSet.getString("owner_weibo_img"));
        blog.setOwnerWeixinImg(resultSet.getString("owner_weixin_img"));
        blog.setOwnerGithub(resultSet.getString("owner_github"));
        blog.setOwnerEmail(resultSet.getString("owner_email"));
        return blog;
    }
}

package com.gxg.dao.impl;

import com.gxg.dao.UserDao;
import com.gxg.dao.rowmapper.UserRowMapper;
import com.gxg.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author 郭欣光
 * @date 2018/10/12 17:34
 */
@Repository(value = "userDao")
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public int getCountById(String id) {
        String sql = "select count(*) from user where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    @Override
    public User getUserById(String id) {
        String sql = "select * from user where id=?";
        User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), id);
        return user;
    }
}

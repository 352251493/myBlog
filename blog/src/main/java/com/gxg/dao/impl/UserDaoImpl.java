package com.gxg.dao.impl;

import com.gxg.dao.UserDao;
import com.gxg.dao.rowmapper.UserRowMapper;
import com.gxg.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * 修改用户
     * @param user
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int updateUser(User user) {
        String sql = "update user set password=?, role=? where id=?";
        int changeCount = jdbcTemplate.update(sql, user.getPassword(), user.getRole(), user.getId());
        return changeCount;
    }

    /**
     * 获取用户数量
     *
     * @return 用户数量
     * @author 郭欣光
     */
    @Override
    public int getCount() {
        String sql = "select count(1) from user";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    /**
     * 添加用户
     *
     * @param user 用户信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int addUser(User user) {
        String sql = "insert into user(id, password, role) values (?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, user.getId(), user.getPassword(), user.getRole());
        return changeCount;
    }

    /**
     * 获取某一角色用户个数
     *
     * @param role 角色
     * @return 用户个数
     * @author 郭欣光
     */
    @Override
    public int getCountByRole(String role) {
        String sql = "select count(1) from user where role=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, role);
        return rowCount;
    }

    /**
     * 根据角色获取用户
     *
     * @param role 角色
     * @return 用户信息
     * @author 郭欣光
     */
    @Override
    public List<User> getUserByRole(String role) {
        String sql = "select * from user where role=?";
        List<User> userList = jdbcTemplate.query(sql, new UserRowMapper(), role);
        return userList;
    }

    /**
     * 删除用户信息
     *
     * @param id 账号
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteUser(String id) {
        String sql = "delete from user where id=?";
        int changeCount = jdbcTemplate.update(sql, id);
        return changeCount;
    }
}

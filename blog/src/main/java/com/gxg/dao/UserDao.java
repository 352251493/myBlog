package com.gxg.dao;

import com.gxg.entities.User;

import java.util.List;

/**
 * @author 郭欣光
 * @date 2018/10/12 17:33
 */
public interface UserDao {

    public int getCountById(String id);

    public User getUserById(String id);

    /**
     * 修改用户
     * @param user
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int updateUser(User user);

    /**
     * 获取用户数量
     * @return 用户数量
     * @author 郭欣光
     */
    public int getCount();

    /**
     * 添加用户
     * @param user 用户信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int addUser(User user);

    /**
     * 获取某一角色用户个数
     * @param role 角色
     * @return 用户个数
     * @author 郭欣光
     */
    public int getCountByRole(String role);

    /**
     * 根据角色获取用户
     * @param role 角色
     * @return 用户信息
     * @author 郭欣光
     */
    public List<User> getUserByRole(String role);

    /**
     * 删除用户信息
     * @param id 账号
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int deleteUser(String id);
}

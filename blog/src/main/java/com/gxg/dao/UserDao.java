package com.gxg.dao;

import com.gxg.entities.User;

/**
 * @author 郭欣光
 * @date 2018/10/12 17:33
 */
public interface UserDao {

    public int getCountById(String id);

    public User getUserById(String id);
}

package com.gxg.service;

import com.gxg.entities.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于用户相关的业务处理接口
 * @author 郭欣光
 * @date 2018/10/12 17:11
 */
public interface UserService {

    /**
     * 用户登录
     * @param id 用户id
     * @param password 用户密码
     * @param checkCode 验证码
     * @param request 用户请求信息
     * @return 登录结果
     * @author 郭欣光
     */
    public String login(String id, String password, String checkCode, HttpServletRequest request);

    /**
     * 获取当前登录的用户信息
     * @param request 用户请求信息
     * @return 如果有用户登录，则返回当前用户信息，否则返回null
     * @author 郭欣光
     */
    public User getLoginUser(HttpServletRequest request);
}

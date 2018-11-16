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

    /**
     * 用户退出
     * @param request 用户请求
     * @return 处理结果
     * @author 郭欣光
     */
    public String signOut(HttpServletRequest request);

    /**
     * 修改密码
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @param repeatNewPassword 重复密码
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    public String resetPassword(String oldPassword, String newPassword, String repeatNewPassword, HttpServletRequest request);

    /**
     * 添加用户
     * @param addUserId 用户账号
     * @param addUserPassword 密码
     * @param addUserRepeatPassword 重复密码
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    public String addUser(String addUserId, String addUserPassword, String addUserRepeatPassword, HttpServletRequest request);

    /**
     * 根据角色获取用户列表
     * @param role 角色
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    public String getUserListByRole(String role, HttpServletRequest request);

    /**
     * 删除用户信息
     * @param userId 用户账号
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    public String deleteUser(String userId, HttpServletRequest request);
}

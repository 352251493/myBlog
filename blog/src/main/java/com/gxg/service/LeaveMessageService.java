package com.gxg.service;

import javax.servlet.http.HttpServletRequest;

/**
 * 留言先关业务处理
 * @author 郭欣光
 * @date 2018/11/13 14:15
 */
public interface LeaveMessageService {

    /**
     * 获得留言列表
     * @param messagePage 页数
     * @param request 用户请求信息
     * @return 留言列表
     * @author 郭欣光
     */
    public String getLeaveMessageList(String messagePage, HttpServletRequest request);

    /**
     * 发送留言验证码
     * @param leaveMessageName 留言者昵称
     * @param email 邮箱
     * @param request 用户请求信息
     * @return 发送结果
     * @author 郭欣光
     */
    public String sendEmailCheckCdoe(String leaveMessageName, String email, HttpServletRequest request);

    /**
     * 验证邮箱验证码并发布留言
     * @param leaveMessageName 留言者昵称
     * @param leaveMessageEmail 留言者邮箱
     * @param leaveMessageComment 留言内容
     * @param leaveMessageEmailCheckCode 邮箱验证码
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    public String checkEmailCheckCodeAndPublish(String leaveMessageName, String leaveMessageEmail, String leaveMessageComment, String leaveMessageEmailCheckCode, HttpServletRequest request);
}

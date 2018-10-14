package com.gxg.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 郭欣光
 * @date 2018/10/12 15:49
 */
public interface CheckCodeService {

    /**
     * 创建验证码
     * @param request
     * @param response
     * @author 郭欣光
     */
    public void createCheckCode(HttpServletRequest request, HttpServletResponse response);
}

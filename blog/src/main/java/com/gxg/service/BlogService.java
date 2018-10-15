package com.gxg.service;

import com.gxg.entities.Blog;

import javax.servlet.http.HttpServletRequest;

/**
 * 提供博客信息相关的逻辑处理
 * @author 郭欣光
 * @date 2018/10/15 15:05
 */
public interface BlogService {

    /**
     * 获得博客相关信息
     * @param request 用户请求信息
     * @return 博客相关信息
     * @author 郭欣光
     */
    public Blog getBlog(HttpServletRequest request);
}

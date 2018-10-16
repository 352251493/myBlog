package com.gxg.service;

import com.gxg.entities.Blog;
import com.gxg.entities.Label;
import com.gxg.entities.ShortWords;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 获得个人博客标签
     * @param request 用户请求信息
     * @return List<Label>个人博客标签
     * @author 郭欣光
     */
    public List<Label> getLabel(HttpServletRequest request);

    /**
     * 获取系统保存Label的最大个数
     * @return 返回系统保存Label的最大个数
     * @author 郭欣光
     */
    public int getSaveLabelCount();

    /**
     * 获得毒鸡汤信息
     * @param request 用户请求信息
     * @return 毒鸡汤信息
     * @author 郭欣光
     */
    public List<ShortWords> getShortWords(HttpServletRequest request);

    /**
     * 获得系统保存毒鸡汤的最大个数
     * @return 系统保存毒鸡汤的最大个数
     * @author 郭欣光
     */
    public int getSaveShortWordsCount();

    /**
     * 添加标签
     * @param name 标签名称
     * @param request 用户请求信息
     * @return 添加处理结果
     * @author 郭欣光
     */
    public String addLabel(String name, HttpServletRequest request);
}

package com.gxg.service;

import com.gxg.entities.Article;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 提供文章相关的业务处理接口
 * @author 郭欣光
 * @date 2018/10/24 15:48
 */
public interface ArticleService {

    /**
     * 获得最近几次的文章列表
     * @return 文章列表
     * @author 郭欣光
     */
    public List<Article> getAllArticleLately();

    /**
     * 文章发表业务处理
     * @param articleTitle 文章标题
     * @param articleAbstract 文章摘要
     * @param articleLabel 文章标签
     * @param articleImg 文章图片
     * @param articleContent 文章内容
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    public String publish(String articleTitle, String articleAbstract, String articleLabel, MultipartFile articleImg, String articleContent, HttpServletRequest request);

    /**
     * 根据标签和页码获取文章列表
     * @param label 文章标签
     * @param page 页码
     * @return 文章列表
     * @author 郭欣光
     */
    public List<Article> getArticleList(String label, String page);

    /**
     * 根据文章标签获得文章总页数
     * @param label 文章标签
     * @return 文章总页数
     * @author 郭欣光
     */
    public int getArticleAllPageNumberByLabel(String label);
}

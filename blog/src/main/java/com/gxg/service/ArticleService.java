package com.gxg.service;

import com.gxg.entities.Article;
import org.json.JSONObject;
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

    /**
     * 获取文章
     * @param articleId 文章id
     * @return json{article:(文章信息), conetnt：(文章内容)}
     * @author 郭欣光
     */
    public JSONObject getArticleDetails(String articleId);

    /**
     * 获得指定文章前一篇文章
     * @param article 指定文章
     * @return 前一篇文章
     * @author 郭欣光
     */
    public  Article getPreviousArticle(Article article);

    /**
     * 获得指定文章的后一篇文章
     * @param article 指定文章
     * @return 后一篇文章
     * @author 郭欣光
     */
    public Article getNextArticle(Article article);

    /**
     * 将文章的阅读数量加一
     * @param article 文章信息
     * @author 郭欣光
     */
    public void addArticleReadCount(Article article);

    /**
     * 根据标签获取最近的文章信息
     * @param label 标签
     * @return 最近文章信息
     * @author 郭欣光
     */
    public List<Article> getLastlyArticleByLabel(String label);

    /**
     * 根据标签获取最热文章信息
     * @param label 标签
     * @return 最热文章信息
     * @author 郭欣光
     */
    public List<Article> getHottestArticleByLabel(String label);

    /**
     * 删除文章
     * @param articleId 文章id
     * @param request 用户请求信息
     * @return 删除结果
     * @author 郭欣光
     */
    public String deleteArticle(String articleId, HttpServletRequest request);

    /**
     * 更改文章封面图片
     * @param articleId 文章id
     * @param articleImg 文章封面图片
     * @param request 用户请求
     * @return 处理结果
     * @author 郭欣光
     */
    public String editImg(String articleId, MultipartFile articleImg, HttpServletRequest request);

    /**
     * 更改文章内容
     * @param articleId 文章ID
     * @param articleTitle 文章标题
     * @param articleAbstract 文章摘要
     * @param articleLabel 文章标签
     * @param articleContent 文章内容
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    public String edit(String articleId, String articleTitle, String articleAbstract, String articleLabel, String articleContent, HttpServletRequest request);

    /**
     * 获取文章评论列表
     * @param articleId 文章ID
     * @param articleCommentPage 获取第几页评论
     * @param request 用户请求信息
     * @return 请求结果
     * @author 郭欣光
     */
    public String getArticleComment(String articleId, String articleCommentPage, HttpServletRequest request);
}

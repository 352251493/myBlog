package com.gxg.service.Impl;

import com.gxg.dao.ArticleCommentDao;
import com.gxg.dao.ArticleDao;
import com.gxg.dao.UserDao;
import com.gxg.entities.Article;
import com.gxg.entities.ArticleComment;
import com.gxg.entities.Blog;
import com.gxg.entities.User;
import com.gxg.service.ArticleService;
import com.gxg.service.BlogService;
import com.gxg.service.MailService;
import com.gxg.utils.FileUtil;
import com.gxg.utils.NumberUtil;
import com.gxg.utils.RegularExpressionUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.multipart.MultipartFile;;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.*;

/**
 * 提供文章相关的业务处理
 * @author 郭欣光
 * @date 2018/10/24 16:06
 */

@Service(value = "articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Value("${article.all.lately.number}")
    private int articleAllLatelyNumber;

    @Value("${article.dir}")
    private String articleDir;

    @Value("${blog.base.dir}")
    private String blogBaseDir;

    private final String blogInformationBaseUrl = "/blog_resource";

    @Autowired
    private UserDao userDao;

    @Value("${article.list.number}")
    private int articleListNumber;

    @Value("${article.same.label.lately.number}")
    private int articleSameLabelLatelyNumber;

    @Value("${article.same.label.hottest.number}")
    private int articleSameLabelHottestNumber;

    @Autowired
    private ArticleCommentDao articleCommentDao;

    @Value("${article.comment.each.page.number}")
    private int articleCommentEachPageNumber;

    @Autowired
    Configuration configuration;//这里注入的是freeMarker的configuration

    @Autowired
    private BlogService blogService;

    @Autowired
    private MailService mailService;

    @Value("${article.comment.head_img.list}")
    private String[] articleCommentHeadImgList;

    /**
     * 获得最近几次的文章列表
     *
     * @return 文章列表
     * @author 郭欣光
     */
    @Override
    public List<Article> getAllArticleLately() {
        if (articleDao.getCount() == 0) {
            return null;
        } else {
            List<Article> articleList = articleDao.getArticleByLimitOrderByModificationTime(0, articleAllLatelyNumber);
            articleList = this.articleUrlProcess(articleList);
            return articleList;
        }
    }

    /**
     * 文章发表业务处理
     *
     * @param articleTitle    文章标题
     * @param articleAbstract 文章摘要
     * @param articleLabel    文章标签
     * @param articleImg      文章图片
     * @param articleContent  文章内容
     * @param request         用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public synchronized String publish(String articleTitle, String articleAbstract, String articleLabel, MultipartFile articleImg, String articleContent, HttpServletRequest request) {
        HttpSession session = request.getSession();
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "发布失败！";
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或登录过期，请刷新页面重新登陆后再次尝试！";
        } else if (articleTitle == null || articleTitle.length() == 0 || "".equals(articleTitle)) {
            content = "标题不能为空！";
        } else if (articleAbstract == null || articleAbstract.length() == 0 || "".equals(articleAbstract)) {
            content = "摘要不能为空！";
        } else if (articleLabel == null || articleLabel.length() == 0 || "".equals(articleLabel)) {
            content = "请选择类别！";
        } else if (articleContent == null || articleContent.length() == 0 || "".equals(articleContent)) {
            content = "请输入正文！";
        } else if (articleTitle.length() > 100) {
            content = "标题不能超过100字符！";
        } else if (articleAbstract.length() > 200) {
            content = "摘要不能超过200字符！";
        } else {
            boolean isImg = true;
            try {
                if (!FileUtil.isImage(articleImg)) {
                    isImg = false;
                    content = "封面图片必须是图片类型！";
                }
            } catch (Exception e) {
                isImg = false;
                content = "系统在验证图片类型时出错！";
            }
            if (isImg) {
                String articleImgName = articleImg.getOriginalFilename();//上传图片的名称
                String articleImgType = articleImgName.substring(articleImgName.lastIndexOf(".") + 1);//上传图片的后缀类型
                if (!FileUtil.isImageByType(articleImgType)) {
                    content = "封面图片必须是图片类型！";
                } else if (FileUtil.getFileSize(articleImg) > 50 * 1024 * 1024) {
                    content = "图片大小不能超过50MB！";
                } else {
                    Article article = new Article();
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    String timeString = time.toString();
                    String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                    while (articleDao.getCountById(id) != 0) {
                        long idLong = Long.parseLong(id);
                        Random random = new Random();
                        idLong += random.nextInt(100);
                        id = idLong + "";
                        if (id.length() > 17) {
                            id = id.substring(0, 17);
                        }
                    }
                    String imgUrl = id + "." + articleImgType;
                    if (articleDir.lastIndexOf("/") != articleDir.length() - 1) {
                        articleDir += "/";
                    }
                    JSONObject uploadImgResult = FileUtil.uploadFile(articleImg, imgUrl, articleDir);
                    if ("true".equals(uploadImgResult.getString("status"))) {
                        article.setImgUrl(imgUrl);
                        String articleUrl = id + ".html";
                        JSONObject writeFileResult = FileUtil.writeFile(articleDir, articleUrl, articleContent);
                        if ("true".equals(writeFileResult.getString("status"))) {
                            article.setArticleUrl(articleUrl);
                            article.setId(id);
                            article.setTitle(articleTitle);
                            article.setArticleAbstract(articleAbstract);
                            article.setLabel(articleLabel);
                            article.setReadNumber(0);
                            article.setCreateTime(time);
                            article.setModificationTime(time);
                            User user = (User)session.getAttribute("user");
                            article.setAuthor(user.getId());
                            try {
                                if (articleDao.createArticle(article) == 0) {
                                    content = "添加数据库失败！";
                                    JSONObject deleteArticleResult = FileUtil.deleteFile(articleDir + articleUrl);
                                    if ("false".equals(deleteArticleResult.getString("status"))) {
                                        System.out.println("发表文章时删除文章出错，原因是：" + deleteArticleResult.getString("content"));
                                    }
                                    JSONObject deleteImgResult = FileUtil.deleteFile(articleDir + imgUrl);
                                    if ("false".equals(deleteImgResult.getString("status"))) {
                                        System.out.println("发表文章时删除图片出错，原因是：" + deleteImgResult.getString("content"));
                                    }
                                } else {
                                    status = "true";
                                    content = "/article/details/" + id;
                                }
                            } catch (Exception e) {
                                content = "添加数据库失败！";
                                System.out.println(e);
                            }
                        } else {
                            content = "发表文章出错，错误原因：" + writeFileResult.getString("content");
                            JSONObject deleteImgResult = FileUtil.deleteFile(articleDir + imgUrl);
                            if ("false".equals(deleteImgResult.getString("status"))) {
                                System.out.println("发表文章时删除图片出错，错误原因：" + deleteImgResult.getString("content"));
                            }
                        }
                    } else {
                        content = "发表文章出错，错误原因：" + uploadImgResult.getString("content");
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 根据标签和页码获取文章列表
     *
     * @param label 文章标签
     * @param page  页码
     * @return 文章列表
     * @author 郭欣光
     */
    @Override
    public List<Article> getArticleList(String label, String page) {
        int pageNumber = 0;
        try {
            pageNumber = Integer.parseInt(page);
        } catch (Exception e) {
            return null;
        }
        if (pageNumber <= 0) {
            return null;
        }
        if (articleDao.getCountByLabel(label) <= (pageNumber - 1) * articleListNumber) {
            return null;
        }
        List<Article> articleList = articleDao.getArticleByLabelAndLLimitOrderByModificationTime(label, (pageNumber - 1) * articleListNumber, articleListNumber);
        articleList = this.articleUrlProcess(articleList);
        return articleList;
    }

    /**
     * 根据文章标签获得文章总页数
     *
     * @param label 文章标签
     * @return 文章总页数
     * @author 郭欣光
     */
    @Override
    public int getArticleAllPageNumberByLabel(String label) {
        int articleCount = articleDao.getCountByLabel(label);
        int allPageNumber = (articleCount % articleListNumber == 0) ? (articleCount / articleListNumber) : (articleCount / articleListNumber + 1);
        allPageNumber = allPageNumber == 0 ? 1 : allPageNumber;
        return allPageNumber;
    }

    private List<Article> articleUrlProcess(List<Article> articleList) {
        this.baseDirProcess();
        if (articleDir.length() >= blogBaseDir.length()) {
            Random random = new Random();
            for (int i = 0; i < articleList.size(); i++) {
                Article article = articleList.get(i);
                if (article.getImgUrl() != null && FileUtil.fileExists(articleDir + article.getImgUrl())) {
                    String imgUrl = blogInformationBaseUrl + articleDir.substring(blogBaseDir.length() - 1) + article.getImgUrl() + "?noCache=" + random.nextDouble();
                    article.setImgUrl(imgUrl);
                }
                if (article.getArticleUrl() != null && FileUtil.fileExists(articleDir + article.getArticleUrl())) {
                    String articleUrl = blogInformationBaseUrl + articleDir.substring(blogBaseDir.length() - 1) + article.getArticleUrl() + "?noCache=" + random.nextDouble();
                    article.setArticleUrl(articleUrl);
                }
                articleList.set(i, article);
            }
        }
        return articleList;
    }

    /**
     * 获取文章
     *
     * @param articleId 文章id
     * @return json{article:(文章信息), conetnt：(文章内容)}
     * @author 郭欣光
     */
    @Override
    public JSONObject getArticleDetails(String articleId) {
        if (articleDao.getCountById(articleId) == 0) {
            return null;
        } else {
            Article article = articleDao.getArticleById(articleId);
            this.baseDirProcess();
            Random random = new Random();
            if (article.getImgUrl() != null && FileUtil.fileExists(articleDir + article.getImgUrl())) {
                String imgUrl = blogInformationBaseUrl + articleDir.substring(blogBaseDir.length() - 1) + article.getImgUrl() + "?noCache=" + random.nextDouble();
                article.setImgUrl(imgUrl);
            }
            if (article.getArticleUrl() != null && FileUtil.fileExists(articleDir + article.getArticleUrl())) {
                JSONObject readFileResult = FileUtil.readFile(articleDir + article.getArticleUrl());
                if ("true".equals(readFileResult.getString("status"))) {
                    String content = readFileResult.getString("content");
                    JSONObject articleDetails = new JSONObject();
                    articleDetails.accumulate("article", article);
                    articleDetails.accumulate("content", content);
                    return articleDetails;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    /**
     * 获得指定文章前一篇文章
     *
     * @param article 指定文章
     * @return 前一篇文章
     * @author 郭欣光
     */
    @Override
    public Article getPreviousArticle(Article article) {
        Article preArticle = null;
        if (article != null && article.getModificationTime() != null && articleDao.getCountByLabelAndMoreThanTheModificationTime(article.getModificationTime(), article.getLabel()) != 0) {
            preArticle = articleDao.getArticleByLabelAndJustMoreThanTheModificationTime(article.getModificationTime(), article.getLabel());
            this.baseDirProcess();
            Random random = new Random();
            if (preArticle.getImgUrl() != null && FileUtil.fileExists(articleDir + preArticle.getImgUrl())) {
                String imgUrl = blogInformationBaseUrl + articleDir.substring(blogBaseDir.length() - 1) + preArticle.getImgUrl() + "?noCache=" + random.nextDouble();
                preArticle.setImgUrl(imgUrl);
            }
            if (preArticle.getArticleUrl() != null && FileUtil.fileExists(articleDir + preArticle.getArticleUrl())) {
                String articleUrl = blogInformationBaseUrl + articleDir.substring(blogBaseDir.length() - 1) + preArticle.getArticleUrl() + "?noCache=" + random.nextDouble();
                preArticle.setArticleUrl(articleUrl);
            }
        }
        return preArticle;
    }

    /**
     * 获得指定文章的后一篇文章
     *
     * @param article 指定文章
     * @return 后一篇文章
     * @author 郭欣光
     */
    @Override
    public Article getNextArticle(Article article) {
        Article nextArticle = null;
        if (article != null && article.getModificationTime() != null && articleDao.getCountByLabelAndLessThanTheModificationTime(article.getModificationTime(), article.getLabel()) != 0) {
            nextArticle = articleDao.getArticleByLabelAndJustLessThanTheModificationTime(article.getModificationTime(), article.getLabel());
            this.baseDirProcess();
            Random random = new Random();
            if (nextArticle.getImgUrl() != null && FileUtil.fileExists(articleDir + nextArticle.getImgUrl())) {
                String imgUrl = blogInformationBaseUrl + articleDir.substring(blogBaseDir.length() - 1) + nextArticle.getImgUrl() + "?noCache=" + random.nextDouble();
                nextArticle.setImgUrl(imgUrl);
            }
            if (nextArticle.getArticleUrl() != null && FileUtil.fileExists(articleDir + nextArticle.getArticleUrl())) {
                String articleUrl = blogInformationBaseUrl + articleDir.substring(blogBaseDir.length() - 1) + nextArticle.getArticleUrl() + "?noCache=" + random.nextDouble();
                nextArticle.setArticleUrl(articleUrl);
            }
        }
        return nextArticle;
    }

    /**
     * 将文章的阅读数量加一
     *
     * @param article 文章信息
     * @author 郭欣光
     */
    @Override
    public synchronized void addArticleReadCount(Article article) {
        if (article != null && articleDao.getCountById(article.getId()) != 0) {
            article = articleDao.getArticleById(article.getId());
            article.setReadNumber(article.getReadNumber() + 1);
            try {
                if (articleDao.updateArticle(article) == 0) {
                    System.out.println("文章：" + article.getId() + "增加阅读量时数据库出错！");
                }
            } catch (Exception e) {
                System.out.println("文章：" + article.getId() + "增加阅读量时数据库出错！错误信息：" + e);
            }
        }
    }

    /**
     * 根据标签获取最近的文章信息
     *
     * @param label 标签
     * @return 最近文章信息
     * @author 郭欣光
     */
    @Override
    public List<Article> getLastlyArticleByLabel(String label) {
        List<Article> latelyArticleList = null;
        if (articleDao.getCountByLabel(label) != 0) {
            latelyArticleList = articleDao.getArticleByLabelAndLLimitOrderByModificationTime(label, 0, articleSameLabelLatelyNumber);
            latelyArticleList = this.articleUrlProcess(latelyArticleList);
        }
        return latelyArticleList;
    }

    /**
     * 根据标签获取最热文章信息
     *
     * @param label 标签
     * @return 最热文章信息
     * @author 郭欣光
     */
    @Override
    public List<Article> getHottestArticleByLabel(String label) {
        List<Article> hottestArticleList = null;
        if (articleDao.getCountByLabel(label) != 0) {
            hottestArticleList = articleDao.getArticleByLabelAndLimitOrderByReadNumber(label, 0, articleSameLabelHottestNumber);
            hottestArticleList = this.articleUrlProcess(hottestArticleList);
        }
        return hottestArticleList;
    }

    /**
     * 删除文章
     *
     * @param articleId 文章id
     * @param request   用户请求信息
     * @return 删除结果
     * @author 郭欣光
     */
    @Override
    public synchronized String deleteArticle(String articleId, HttpServletRequest request) {
        HttpSession session = request.getSession();
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或登录过期，请刷新页面重新登陆后再次尝试！";
        } else if (articleDao.getCountById(articleId) == 0) {
            content = "该文章不存在！";
        } else {
            Article article = articleDao.getArticleById(articleId);
            boolean isDelete = true;
            try {
                if (articleDao.deleteArticle(article) == 0) {
                    isDelete= false;
                    System.out.println("从删除文章：" + articleId + "出错！");
                    content = "从数据库删除文章出错！";
                }
            } catch (Exception e) {
                isDelete = false;
                System.out.println("从删除文章：" + articleId + "出错！错误原因：" + e);
                content = "从数据库删除文章出错！";
            }
            if (isDelete) {
                status = "true";
                if ("生活情感".equals(article.getLabel())) {
                    content = "/life/";
                } else if ("学习感悟".equals(article.getLabel())) {
                    content = "/study/";
                } else {
                    content = "/";
                }
                this.baseDirProcess();
                if (FileUtil.fileExists(articleDir + article.getImgUrl())) {
                    JSONObject deleteImgResult = FileUtil.deleteFile(articleDir + article.getImgUrl());
                    System.out.println("删除文章" + articleId + "的封面图片结果：" + deleteImgResult.toString());
                } else {
                    System.out.println("文章" + articleId + "封面图片不存在！");
                }
                if (FileUtil.fileExists(articleDir + article.getArticleUrl())) {
                    JSONObject deleteArticleResult = FileUtil.deleteFile(articleDir + article.getArticleUrl());
                    System.out.println("删除文章" + articleId + "内容文件结果：" + deleteArticleResult);
                } else {
                    System.out.println("文章" + articleId + "内容文件不存在！");
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private void baseDirProcess() {
        if (articleDir.lastIndexOf("/") != articleDir.length() - 1) {
            articleDir += "/";
        }
        if (blogBaseDir.lastIndexOf("/") != blogBaseDir.length() - 1) {
            blogBaseDir += "/";
        }
    }

    /**
     * 更改文章封面图片
     *
     * @param articleId  文章id
     * @param articleImg 文章封面图片
     * @param request    用户请求
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public synchronized String editImg(String articleId, MultipartFile articleImg, HttpServletRequest request) {
        HttpSession session = request.getSession();
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "更改失败！";
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或登录过期，请刷新页面重新登陆后再次尝试！";
        } else if (articleId == null || articleId.length() == 0  || "".equals(articleId)) {
            content = "系统获取文章ID失败！";
        } else if (articleDao.getCountById(articleId) == 0) {
            content = "该文章不存在！";
        } else {
            boolean isImg = true;
            try {
                if (!FileUtil.isImage(articleImg)) {
                    isImg = false;
                    content = "请上传图片类型文件！";
                }
            } catch (Exception e) {
                System.out.println("系统在判断文件是否是图片时出错：" + e);
                content = "系统在判断文件是否是图片时出错！";
                isImg = false;
            }
            if (isImg) {
                String articleImgName = articleImg.getOriginalFilename();//上传图片的名称
                String articleImgType = articleImgName.substring(articleImgName.lastIndexOf(".") + 1);//上传图片的后缀类型
                if (!FileUtil.isImageByType(articleImgType)) {
                    content = "请上传图片类型文件！";
                } else if (FileUtil.getFileSize(articleImg) > 50 * 1024 * 1024) {
                    content = "图片大小不能超过50MB！";
                } else {
                    Article article = articleDao.getArticleById(articleId);
                    this.baseDirProcess();
                    JSONObject uploadImgResult = FileUtil.uploadFile(articleImg, article.getId() + "." + articleImgType, articleDir);
                    if ("true".equals(uploadImgResult.getString("status"))) {
                        String oldArticleImgType = article.getImgUrl().substring(article.getImgUrl().lastIndexOf(".") + 1);
                        Timestamp time = new Timestamp(System.currentTimeMillis());
                        article.setModificationTime(time);
                        if (articleImgType.equals(oldArticleImgType)) {
                            status = "true";
                            content = "更改成功！";
                            try {
                                if (articleDao.updateArticle(article) == 0) {
                                    System.out.println("更改文章" + article.getId() + "封面图片时更新数据库失败，导致更新修改时间未成功");
                                }
                            } catch (Exception e) {
                                System.out.println("更改文章" + article.getId() + "封面图片时更新数据库失败，导致更新修改时间未成功，失败原因：" + e);
                            }
                        } else {
                            article.setImgUrl(article.getId() + "." + articleImgType);
                            try {
                                articleDao.updateArticle(article);
                                status = "true";
                                content = "更改成功！";
                            } catch (Exception e) {
                                System.out.println("更改文章" + article.getId() + "封面图片时更新数据库失败，失败原因：" + e);
                                content = "更新数据库失败！";
                            }
                            if ("true".equals(status)) {
                                JSONObject deleteOldImgResult = FileUtil.deleteFile(articleDir + article.getId() + "." + oldArticleImgType);
                                System.out.println("更改文章" + article.getId() + "封面图片时删除原图片" + articleDir + article.getId() + "." + oldArticleImgType + "处理结果：" + deleteOldImgResult.toString());
                            } else {
                                JSONObject deleteUploadImgResult = FileUtil.deleteFile(articleDir + article.getId() + "." + articleImgType);
                                System.out.println("更改文章" + article.getId() + "数据库操作失败后删除上传的图片" + articleDir + article.getId() + "." + articleImgType + "处理结果：" + deleteUploadImgResult.toString());
                            }
                        }
                    } else {
                        status = "false";
                        content = uploadImgResult.getString("content");
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

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
    @Override
    public synchronized String edit(String articleId, String articleTitle, String articleAbstract, String articleLabel, String articleContent, HttpServletRequest request) {
        HttpSession session = request.getSession();
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "修改失败！";
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或登录过期，请刷新页面重新登陆后再次尝试！";
        } else if (articleId == null || articleId.length() == 0 || "".equals(articleId)) {
            content = "系统获取文章ID失败！";
        } else if (articleDao.getCountById(articleId) == 0) {
            content = "该文章不存在！";
        } else if (articleTitle == null || articleTitle.length() == 0 || "".equals(articleTitle)) {
            content = "标题不能为空！";
        } else if (articleAbstract == null || articleAbstract.length() == 0 || "".equals(articleAbstract)) {
            content = "摘要不能为空！";
        } else if (articleLabel == null || articleLabel.length() == 0 || "".equals(articleLabel)) {
            content = "请选择类别！";
        } else if (articleContent == null || articleContent.length() == 0 || "".equals(articleContent)) {
            content = "请输入正文！";
        } else if (articleTitle.length() > 100) {
            content = "标题不能超过100字符！";
        } else if (articleAbstract.length() > 200) {
            content = "摘要不能超过200字符！";
        } else {
            this.baseDirProcess();
            JSONObject writeArticleResult = FileUtil.writeFile(articleDir, articleId + ".html", articleContent);
            if ("true".equals(writeArticleResult.getString("status"))) {
                Article article = articleDao.getArticleById(articleId);
                Timestamp time = new Timestamp(System.currentTimeMillis());
                article.setTitle(articleTitle);
                article.setArticleAbstract(articleAbstract);
                article.setLabel(articleLabel);
                article.setModificationTime(time);
                try {
                    if (articleDao.updateArticle(article) == 0) {
                        System.out.println("修改文章" + articleId + "时操作数据库失败，但文章正文已经保存！");
                        content = "修改文章时操作数据库失败，但文章正文已经保存！";
                    } else {
                        status = "true";
                        content = "/article/details/" + articleId;
                    }
                } catch (Exception e) {
                    System.out.println("修改文章" + articleId + "时操作数据库失败，但文章正文已经保存！失败原因：" + e);
                    content = "修改文章时操作数据库失败，但文章正文已经保存！";
                }
            } else {
                content = "修改失败，原因：" + writeArticleResult.getString("content");
            }

        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 获取文章评论列表
     *
     * @param articleId          文章ID
     * @param articleCommentPage 获取第几页评论
     * @param request            用户请求信息
     * @return 请求结果
     * @author 郭欣光
     */
    @Override
    public String getArticleComment(String articleId, String articleCommentPage, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "获取失败！";
        String isUser = "false";
        String isHasComment = "false";
        int articleCommentPageInt = 0;
        List<ArticleComment> articleCommentList = null;
        try {
            articleCommentPageInt = Integer.parseInt(articleCommentPage);
        } catch (Exception e) {
            content = "传入的页数不是整型数字！";
        }
        if (articleCommentPageInt <= 0) {
            if ("获取失败！".equals(content)) {
                content = "传入的页数小于或等于0！";
            }
        } else if (articleCommentDao.getCountByArticleId(articleId) == 0) {
            content = "没有评论！";
        } else if (articleCommentDao.getCountByArticleId(articleId) <= articleCommentEachPageNumber * (articleCommentPageInt - 1)) {
            status = "true";
            content = "没有更多评论了";
        } else {
            articleCommentList = articleCommentDao.getArticleCommentByArticleIdAndLimitOrderByCreateTime(articleId, articleCommentEachPageNumber * (articleCommentPageInt - 1), articleCommentEachPageNumber);
            status = "true";
            isHasComment = "true";
            HttpSession session = request.getSession();
            if (session.getAttribute("user") != null) {
                isUser = "true";
            }
        }
        result.accumulate("status", status);
        result.accumulate("isUser", isUser);
        result.accumulate("isHasComment", isHasComment);
        if (articleCommentList != null) {
            result.accumulate("content", articleCommentList);
        } else {
            result.accumulate("content", content);
        }
        return result.toString();
    }

    /**
     * 向邮箱发送验证码
     * @param articleCommentName 评论人昵称
     * @param email 邮箱
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String sendEmailCheckCode(String articleCommentName, String email, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "发送验证码失败！";
        if (email == null || "".equals(email) || email.length() == 0) {
            content = "邮箱信息为空！";
        } else if (RegularExpressionUtil.checkEmail(email)) {
            String articleCommentEmailCheckCode = NumberUtil.makeNumber(6);
            HttpSession session = request.getSession();
            session.setAttribute("articleCommentEmailCheckCode", articleCommentEmailCheckCode);
            Blog blog = blogService.getBlog(request);
            String fromUserName = "随遇而安。";
            if (blog != null && blog.getOwnerName() != null && !"".equals(blog.getOwnerName())) {
                fromUserName = blog.getOwnerName();
            }
            String toUserName = "有缘人";
            if (articleCommentName != null && articleCommentName.length() != 0 && !"".equals(articleCommentName)) {
                toUserName = articleCommentName;
            }
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("time", new Date());
            model.put("articleCommentEmailCheckCode", articleCommentEmailCheckCode);
            model.put("toUserName", toUserName);
            model.put("fromUserName", fromUserName);
            try {
                Template t = configuration.getTemplate("email_check_code.ftl");
                String emailContent = FreeMarkerTemplateUtils.processTemplateIntoString(t, model);
                JSONObject sendEmailResult = mailService.sendHtmlMail(email, "邮箱验证", emailContent);
                status = sendEmailResult.getString("status");
                content = sendEmailResult.getString("content");
            } catch (Exception e) {
                System.out.println("发送邮件失败，失败原因：" + e);
                content = "发送邮件失败！";
            }
        } else {
            content = "邮箱格式不正确！";
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 检查邮箱验证码并发表评论
     * @param articleId 文章ID
     * @param articleCommentName 文章评论昵称
     * @param articleCommentEmail 文章评论邮箱
     * @param articleCommentComment 文章评论内容
     * @param articleCommentEmailCheckCode 用户填写的验证码
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public synchronized String checkEmailCheckCodeAndPublish(String articleId, String articleCommentName, String articleCommentEmail, String articleCommentComment, String articleCommentEmailCheckCode, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "发表评论出错！";
        if (articleId == null || "".equals(articleId) || articleId.length() == 0) {
            content = "系统获取文章ID出错！";
        } else if (articleCommentName == null || "".equals(articleCommentName) || articleCommentName.length() == 0) {
            content = "请留下您的昵称";
        } else if (articleCommentEmail == null || "".equals(articleCommentEmail) || articleCommentEmail.length() == 0) {
            content = "请留下您的邮箱，您的邮箱信息将不会被其他人看到，该邮箱仅用于对您提出的意见进行探讨";
        } else if (articleCommentComment == null || "".equals(articleCommentComment) || articleCommentComment.length() == 0) {
            content = "说点什么呗~";
        } else if (articleCommentEmailCheckCode == null || "".equals(articleCommentEmailCheckCode) || articleCommentEmailCheckCode.length() == 0) {
            content = "请输入验证码！";
        } else if (articleCommentName.length() > 20) {
            content = "昵称长度不能大于20字符";
        } else if (!RegularExpressionUtil.checkEmail(articleCommentEmail)) {
            content = "您输入的邮箱格式不正确，您的邮箱信息将不会被其他人看到，该邮箱仅用于对您提出的意见进行探讨";
        } else if (articleCommentEmail.length() > 50) {
            content = "邮箱长度不能大于50字符";
        } else if (articleCommentComment.length() > 300) {
            content = "评论内容不能超过300字符";
        } else if (articleDao.getCountById(articleId) == 0) {
            content = "该文章不存在！";
        } else if (articleCommentHeadImgList.length == 0) {
            System.out.println("系统内文章评论头像列表为空，无法分配头像！");
            content = "系统内文章评论头像列表为空，无法分配头像！";
        } else {
            HttpSession session = request.getSession();
            if (session.getAttribute("articleCommentEmailCheckCode") == null) {
                content = "验证码信息已失效！";
            } else {
                String systemArticleCommentEmailCheckCode = (String)session.getAttribute("articleCommentEmailCheckCode");
                if (articleCommentEmailCheckCode.equals(systemArticleCommentEmailCheckCode)) {
                    ArticleComment articleComment = new ArticleComment();
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    String timeString = time.toString();
                    String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                    while (articleCommentDao.getCountById(id) != 0) {
                        long idLong = Long.parseLong(id);
                        Random random = new Random();
                        idLong += random.nextInt(100);
                        id = idLong + "";
                        if (id.length() > 17) {
                            id = id.substring(0, 17);
                        }
                    }
                    articleComment.setId(id);
                    articleComment.setComment(articleCommentComment);
                    articleComment.setName(articleCommentName);
                    articleComment.setEmail(articleCommentEmail);
                    articleComment.setArticleId(articleId);
                    articleComment.setCreateTime(time);
                    Random random = new Random();
                    int headImgIndex = random.nextInt(articleCommentHeadImgList.length);
                    String headImg = articleCommentHeadImgList[headImgIndex];
                    articleComment.setHeadImg(headImg);
                    try {
                        if (articleCommentDao.createArticleComment(articleComment) == 0) {
                            System.out.println("文章" + articleId + "评论" + id + "添加数据库出错");
                            content = "添加数据库出错！";
                        } else {
                            status = "true";
                            content = "发表成功！";
                        }
                    } catch (Exception e) {
                        System.out.println("文章" + articleId + "评论" + id + "添加数据库出错，错误原因：" + e);
                        content = "添加数据库出错！";
                    }
                } else {
                    content = "验证码不正确！";
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }
}
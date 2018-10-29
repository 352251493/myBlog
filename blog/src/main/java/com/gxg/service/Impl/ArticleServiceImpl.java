package com.gxg.service.Impl;

import com.gxg.controller.ArticleController;
import com.gxg.dao.ArticleDao;
import com.gxg.dao.UserDao;
import com.gxg.entities.Article;
import com.gxg.entities.User;
import com.gxg.service.ArticleService;
import com.gxg.utils.FileUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

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
                                    content = "发表成功！";
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
        if (articleDir.lastIndexOf("/") != articleDir.length() - 1) {
            articleDir += "/";
        }
        if (blogBaseDir.lastIndexOf("/") != blogBaseDir.length() - 1) {
            blogBaseDir += "/";
        }
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
            if (articleDir.lastIndexOf("/") != articleDir.length() - 1) {
                articleDir += "/";
            }
            if (blogBaseDir.lastIndexOf("/") != blogBaseDir.length() - 1) {
                blogBaseDir += "/";
            }
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
}
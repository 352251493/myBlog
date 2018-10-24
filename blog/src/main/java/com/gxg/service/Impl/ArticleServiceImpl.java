package com.gxg.service.Impl;

import com.gxg.dao.ArticleDao;
import com.gxg.dao.UserDao;
import com.gxg.entities.Article;
import com.gxg.entities.User;
import com.gxg.service.ArticleService;
import com.gxg.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    }
}

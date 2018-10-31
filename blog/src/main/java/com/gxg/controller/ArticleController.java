package com.gxg.controller;

import com.gxg.entities.*;
import com.gxg.service.ArticleService;
import com.gxg.service.BlogService;
import com.gxg.service.UserService;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 与文章相关的请求接受与响应
 * @author 郭欣光
 * @date 2018/10/25 11:12
 */
@Controller
@RequestMapping(value = "/article")
public class ArticleController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ArticleService articleService;

    @GetMapping(value = "/publish")
    public String publish(Model model, HttpServletRequest request) {
        model.addAttribute("pageName", "发表文章");
        User user = userService.getLoginUser(request);
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            return "/error/no_user_login.html";
        }
        model = this.setBaseModel(model, request);
//        List<Article> articleList = articleService.getAllArticleLately();
//        model.addAttribute("articleList", articleList);
        return "/article_publish.html";
    }

    @PostMapping(value = "/publish")
    @ResponseBody
    public String publish(@RequestParam String articleTitle, @RequestParam String articleAbstract, @RequestParam String articleLabel, @RequestParam MultipartFile articleImg, @RequestParam String articleContent, HttpServletRequest request) {
        return articleService.publish(articleTitle, articleAbstract, articleLabel, articleImg, articleContent, request);
    }

    @GetMapping(value = "/details/{articleId}")
    public String details(@PathVariable String articleId, Model model, HttpServletRequest request) {
        JSONObject articleDetails = articleService.getArticleDetails(articleId);
        if (articleDetails == null) {
            model.addAttribute("errorTitle", "页面出错啦");
            model.addAttribute("errorMessage", "该文章不存在或者读取该文章时出现系统错误");
            return "/error/std_error.html";
        } else {
            Article article = (Article)articleDetails.get("article");
            model.addAttribute("article", article);
            model.addAttribute("articleDetails", articleDetails.getString("content"));
            model.addAttribute("pageName", article.getLabel());
            Blog blog = blogService.getBlog(request);
            if (blog != null) {
                model.addAttribute("blog", blog);
            }
            User user = userService.getLoginUser(request);
            if (user != null) {
                model.addAttribute("user", user);
            }
            Article preArticle = articleService.getPreviousArticle(article);
            model.addAttribute("preArticle", preArticle);
            Article nextArticle = articleService.getNextArticle(article);
            model.addAttribute("nextArticle", nextArticle);
            articleService.addArticleReadCount(article);
            List<Article> latelyArticleList = articleService.getLastlyArticleByLabel(article.getLabel());
            model.addAttribute("latelyArticleList", latelyArticleList);
            List<Article> hottestArticleList = articleService.getHottestArticleByLabel(article.getLabel());
            model.addAttribute("hottestArticleList", hottestArticleList);
            return "/article.html";
        }
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public String delete(@RequestParam String articleId, HttpServletRequest request) {
        return articleService.deleteArticle(articleId, request);
    }

    @PostMapping(value = "/edit/img")
    @ResponseBody
    public String editImg(@RequestParam String articleId, @RequestParam MultipartFile articleImg, HttpServletRequest request) {
        return articleService.editImg(articleId, articleImg, request);
    }

    @GetMapping(value = "/edit/{articleId}")
    public String edit(@PathVariable String articleId, HttpServletRequest request, Model model) {
        User user = userService.getLoginUser(request);
        if (user == null) {
            return "/error/no_user_login.html";
        } else {
            model.addAttribute("user", user);
        }
        JSONObject articleDetails = articleService.getArticleDetails(articleId);
        if (articleDetails == null) {
            model.addAttribute("errorTitle", "页面出错啦");
            model.addAttribute("errorMessage", "该文章不存在或者读取该文章时出现系统错误");
            return "/error/std_error.html";
        }
        model = this.setBaseModel(model, request);
        Article article = (Article)articleDetails.get("article");
        model.addAttribute("article", article);
        model.addAttribute("articleDetails", articleDetails.getString("content"));
        model.addAttribute("pageName", article.getLabel());
        return "/article_edit.html";
    }

    @PostMapping(value = "/edit")
    @ResponseBody
    public String edit(@RequestParam String articleId, @RequestParam String articleTitle, @RequestParam String articleAbstract, @RequestParam String articleLabel, @RequestParam String articleContent, HttpServletRequest request) {
        return articleService.edit(articleId, articleTitle, articleAbstract, articleLabel, articleContent, request);
    }

    private Model setBaseModel(Model model, HttpServletRequest request) {
        Blog blog = blogService.getBlog(request);
        if (blog != null) {
            model.addAttribute("blog", blog);
        }
        List<Label> labelList = blogService.getLabel(request);
        if (labelList != null) {
            model.addAttribute("labelList", labelList);
        }
        int labelCount = blogService.getSaveLabelCount();
        model.addAttribute("labelCount", labelCount);
        List<ShortWords> shortWordsList = blogService.getShortWords(request);
        if (shortWordsList != null) {
            model.addAttribute("shortWordsList", shortWordsList);
        }
        int shortWordsCount = blogService.getSaveShortWordsCount();
        model.addAttribute("shortWordsCount", shortWordsCount);
        return model;
    }
}

package com.gxg.controller;

import com.gxg.entities.*;
import com.gxg.service.ArticleService;
import com.gxg.service.BlogService;
import com.gxg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
        Blog blog = blogService.getBlog(request);
        if (blog != null) {
            model.addAttribute("blog", blog);
        }
        User user = userService.getLoginUser(request);
        if (user != null) {
            model.addAttribute("user", user);
        } else {
            return "/error/no_user_login.html";
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
        List<Article> articleList = articleService.getAllArticleLately();
        model.addAttribute("articleList", articleList);
        return "/article_publish.html";
    }

    @PostMapping(value = "/publish")
    @ResponseBody
    public String publish(@RequestParam String articleTitle, @RequestParam String articleAbstract, @RequestParam String articleLabel, @RequestParam MultipartFile articleImg, @RequestParam String articleContent, HttpServletRequest request) {
        return articleService.publish(articleTitle, articleAbstract, articleLabel, articleImg, articleContent, request);
    }
}

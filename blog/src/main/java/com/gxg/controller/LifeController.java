package com.gxg.controller;

import com.gxg.entities.*;
import com.gxg.service.ArticleService;
import com.gxg.service.BlogService;
import com.gxg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 郭欣光
 * @date 2018/10/15 11:29
 */

@Controller
@RequestMapping(value = "/life")
public class LifeController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ArticleService articleService;

    @RequestMapping(value = "")
    public String life(Model model, HttpServletRequest request) {
        model = this.globelModelProcess(model, request);
        List<Article> articleList = articleService.getArticleList("生活情感", "1");
        model.addAttribute("articleList", articleList);
        model.addAttribute("pageNumber", 1);
        int allPageNumber = articleService.getArticleAllPageNumberByLabel("生活情感");
        model.addAttribute("allPageNumber", allPageNumber);
        if (allPageNumber > 1) {
            model.addAttribute("nextPageNumber", 2);
        }
        return "/article_list.html";
    }

    @GetMapping(value = "/page/{pageNumber}")
    public String life(Model model, HttpServletRequest request, HttpServletResponse response, @PathVariable String pageNumber) {
        model = this.globelModelProcess(model, request);
        int pageIntNumber = 0;
        try {
            pageIntNumber = Integer.parseInt(pageNumber);
        } catch (Exception e) {
            try {
                response.sendRedirect("/life/");
            } catch (Exception e1) {
                System.out.println("访问生活情感page超页时：" + e1);
            }
        }
        List<Article> articleList = articleService.getArticleList("生活情感", pageNumber);
        if (articleList == null && !"1".equals(pageNumber.trim())) {
            try {
                response.sendRedirect("/life/");
            } catch (Exception e) {
                System.out.println("访问生活情感page超页时：" + e);
            }
        }
        model.addAttribute("articleList", articleList);
        model.addAttribute("pageNumber", pageIntNumber);
        int allPageNumber = articleService.getArticleAllPageNumberByLabel("生活情感");
        model.addAttribute("allPageNumber", allPageNumber);
        if (pageIntNumber > 1) {
            model.addAttribute("prePageNumber", pageIntNumber - 1);
        }
        if (pageIntNumber < allPageNumber && pageIntNumber >= 0) {
            model.addAttribute("nextPageNumber", pageIntNumber + 1);
        }
        return "/article_list.html";
    }

    private Model globelModelProcess(Model model, HttpServletRequest request) {
        model.addAttribute("pageName", "生活情感");
        User user = userService.getLoginUser(request);
        if (user != null) {
            model.addAttribute("user", user);
        }
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

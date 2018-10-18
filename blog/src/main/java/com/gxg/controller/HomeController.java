package com.gxg.controller;

import com.gxg.entities.Blog;
import com.gxg.entities.Label;
import com.gxg.entities.ShortWords;
import com.gxg.entities.User;
import com.gxg.service.BlogService;
import com.gxg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 关于主页的Controller类
 * @author 郭欣光
 * @date 2018/10/12 12:17
 */

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        model.addAttribute("pageName", "首页");
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
        return "/index.html";
    }
}

package com.gxg.controller;

import com.gxg.entities.Blog;
import com.gxg.entities.User;
import com.gxg.service.BlogService;
import com.gxg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

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

    @RequestMapping(value = "")
    public String life(Model model, HttpServletRequest request) {
        model.addAttribute("pageName", "生活情感");
        User user = userService.getLoginUser(request);
        if (user != null) {
            model.addAttribute("user", user);
        }
        Blog blog = blogService.getBlog(request);
        if (blog != null) {
            model.addAttribute("blog", blog);
        }
        return "/life.html";
    }
}

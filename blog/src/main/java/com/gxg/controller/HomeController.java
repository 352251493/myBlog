package com.gxg.controller;

import com.gxg.entities.User;
import com.gxg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 关于主页的Controller类
 * @author 郭欣光
 * @date 2018/10/12 12:17
 */

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model, HttpServletRequest request) {
        model.addAttribute("pageName", "首页");
        User user = userService.getLoginUser(request);
        if (user != null) {
            model.addAttribute("user", user);
        }
        return "/index.html";
    }
}

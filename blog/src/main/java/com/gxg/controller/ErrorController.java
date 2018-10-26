package com.gxg.controller;

import com.gxg.entities.Blog;
import com.gxg.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 郭欣光
 * @date 2018/10/26 16:58
 */

@Controller
@RequestMapping(value = "/error")
public class ErrorController {

    @Autowired
    private BlogService blogService;

    @RequestMapping(value = "/{errorCode}")
    public String error(@PathVariable int errorCode, Model model, HttpServletRequest request) {
        Blog blog = blogService.getBlog(request);
        if (blog != null) {
            model.addAttribute("blog", blog);
        }
        switch (errorCode) {
            case 404:
                model.addAttribute("errorTitle", "404，页面不存在");
                model.addAttribute("errorMessage", "您访问的页面去火星啦");
                break;
            case 500:
                model.addAttribute("errorTitle", "500，服务器出错啦~");
                model.addAttribute("errorMessage", "服务器好像出了一点小状况");
                break;
            default:
                model.addAttribute("errorTitle", "出错啦！");
                model.addAttribute("errorMessage", "该页面暂无法访问");
                break;
        }
        return "/error/std_error.html";
    }
}

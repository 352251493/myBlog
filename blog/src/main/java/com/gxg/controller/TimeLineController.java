package com.gxg.controller;

import com.gxg.entities.Blog;
import com.gxg.entities.User;
import com.gxg.service.BlogService;
import com.gxg.service.TimeLineService;
import com.gxg.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 时间轴页面相关请求控制
 * @author 郭欣光
 * @date 2018/11/12 10:56
 */

@Controller
@RequestMapping(value = "/timeline")
public class TimeLineController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private UserService userService;

    @Autowired
    private TimeLineService timeLineService;

    @GetMapping(value = "")
    public String timeLine(Model model, HttpServletRequest request) {
        model.addAttribute("pageName", "时间轴");
        User user = userService.getLoginUser(request);
        if (user != null) {
            model.addAttribute("user", user);
        }
        Blog blog = blogService.getBlog(request);
        if (blog != null) {
            model.addAttribute("blog", blog);
        }
        JSONObject timeLine = timeLineService.getTimeLine("1");
        model.addAttribute("timeLine", timeLine);
        return "/timeline.html";
    }

    @GetMapping(value = "/page/{pageNumber}")
    public String timeLine(@PathVariable String pageNumber, Model model, HttpServletRequest request) {
        int pageNumberInt = 0;
        try {
            pageNumberInt = Integer.parseInt(pageNumber);
        } catch (Exception e) {
            System.out.println("访问时间轴页面的页码不正确：" + e);
            return "redict:/timeline/";
        }
        JSONObject timeLine = timeLineService.getTimeLine(pageNumber);
        if (timeLine == null && !"1".equals(pageNumber)) {
            return "redict:/timeline/";
        }
        model.addAttribute("pageName", "时间轴");
        User user = userService.getLoginUser(request);
        if (user != null) {
            model.addAttribute("user", user);
        }
        Blog blog = blogService.getBlog(request);
        if (blog != null) {
            model.addAttribute("blog", blog);
        }
        model.addAttribute("timeLine", timeLine);
        return "/timeline.html";
    }
}

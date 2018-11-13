package com.gxg.controller;

import com.gxg.entities.Blog;
import com.gxg.entities.User;
import com.gxg.service.BlogService;
import com.gxg.service.LeaveMessageService;
import com.gxg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 留言板相关请求响应控制
 * @author 郭欣光
 * @date 2018/11/13 9:57
 */
@Controller
@RequestMapping(value = "/message")
public class MessageController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private LeaveMessageService leaveMessageService;

    @GetMapping(value = "")
    public String message(Model model, HttpServletRequest request) {
        model.addAttribute("pageName", "留言吧");
        User user = userService.getLoginUser(request);
        if (user != null) {
            model.addAttribute("user", user);
        }
        Blog blog = blogService.getBlog(request);
        if (blog != null) {
            model.addAttribute("blog", blog);
        }
        return "/leave_message.html";
    }

    @PostMapping(value = "/list")
    @ResponseBody
    public String list(@RequestParam String messagePage, HttpServletRequest request) {
        return leaveMessageService.getLeaveMessageList(messagePage, request);
    }

    @PostMapping(value = "/email/check_code/send")
    @ResponseBody
    public String sendEmailCheckCode(@RequestParam String leaveMessageName, @RequestParam String email, HttpServletRequest request) {
        return leaveMessageService.sendEmailCheckCdoe(leaveMessageName, email, request);
    }

    @PostMapping(value = "/email/check_code/check_and_publish")
    @ResponseBody
    public String checkEmailCheckCodeAndPublish(@RequestParam String leaveMessageName, @RequestParam String leaveMessageEmail, @RequestParam String leaveMessageComment, @RequestParam String leaveMessageEmailCheckCode, HttpServletRequest request) {
        return leaveMessageService.checkEmailCheckCodeAndPublish(leaveMessageName, leaveMessageEmail, leaveMessageComment, leaveMessageEmailCheckCode, request);
    }
}

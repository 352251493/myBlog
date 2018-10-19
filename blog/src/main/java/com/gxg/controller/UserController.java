package com.gxg.controller;

import com.gxg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于User的Controller类
 * @author 郭欣光
 * @date 2018/10/12 17:04
 */

@Controller
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 处理用户的登录请求
     * @param id 用户id
     * @param password 用户密码
     * @param checkCode 验证码
     * @param request 用户请求信息
     * @return 登录结果
     */
    @PostMapping(value = "/login")
    @ResponseBody
    public String login(@RequestParam String id, @RequestParam String password, @RequestParam String checkCode, HttpServletRequest request) {
        return userService.login(id, password, checkCode, request);
    }

    @PostMapping(value = "/sign_out")
    @ResponseBody
    public String signOut(HttpServletRequest request) {
        return userService.signOut(request);
    }
}

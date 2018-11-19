package com.gxg.controller;

import com.gxg.entities.User;
import com.gxg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/get_user")
    @ResponseBody
    public String getUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        if (user == null) {
            return "Get user failure!";
        } else {
            return "Get user success!";
        }
    }

    @PostMapping(value = "/password/reset")
    @ResponseBody
    public String resetPassword(@RequestParam String oldPassword, @RequestParam String newPassword, @RequestParam String repeatNewPassword, HttpServletRequest request) {
        return userService.resetPassword(oldPassword, newPassword, repeatNewPassword, request);
    }

    @PostMapping(value = "/add")
    @ResponseBody
    public String addUser(@RequestParam String addUserId, @RequestParam String addUserPassword, @RequestParam String addUserRepeatPassword, HttpServletRequest request) {
        return userService.addUser(addUserId, addUserPassword, addUserRepeatPassword, request);
    }

    @GetMapping(value = "/list")
    @ResponseBody
    public String getUserList(HttpServletRequest request) {
        return userService.getUserListByRole("编辑", request);
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public String deleteUser(@RequestParam String userId, HttpServletRequest request) {
        return userService.deleteUser(userId, request);
    }

    @PostMapping(value = "/edit/password")
    @ResponseBody
    public String editUserPassword(@RequestParam String userId, @RequestParam String password, HttpServletRequest request) {
        return userService.editUserPassword(userId, password, request);
    }
}

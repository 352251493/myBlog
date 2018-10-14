package com.gxg.service.Impl;

import com.gxg.dao.UserDao;
import com.gxg.entities.User;
import com.gxg.service.UserService;
import com.gxg.utils.Md5;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用于用户相关的业务处理类
 * @author 郭欣光
 * @date 2018/10/12 17:14
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 用户登录
     *
     * @param id        用户id
     * @param password  用户密码
     * @param checkCode 验证码
     * @param request   用户请求信息
     * @return 登录结果
     * @author 郭欣光
     */
    @Override
    public String login(String id, String password, String checkCode, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "登陆失败！";
        if (id == null || id.length() == 0 || "".equals(id)) {
            content = "账号不能为空！";
        } else if (id.length() > 8) {
            System.out.println(id.length());
            content = "账号长度不能大于8个字符！";
        } else if (password == null || password.length() == 0 || "".equals(password)) {
            content = "密码不能为空！";
        } else if (checkCode == null || checkCode.length() == 0 || "".equals(checkCode)) {
            content = "验证码不能为空！";
        } else {
            HttpSession session = request.getSession();
            if (session.getAttribute("checkCode") == null) {
                content = "验证码系统出错！";
            } else {
                String systemCheckCode = (String)session.getAttribute("checkCode");
                if (systemCheckCode.equals(checkCode)) {
                    if (userDao.getCountById(id) == 0) {
                        content = "该用户不存在！";
                    } else {
                        User user = userDao.getUserById(id);
                        if (user.getPassword().equals(Md5.md5(password))) {
                            session.setAttribute("user", user);
                            status = "true";
                            content = "登录成功！";
                        } else {
                            content = "密码错误！";
                        }
                    }
                } else {
                    content = "验证码错误！";
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 获取当前登录的用户信息
     *
     * @param request 用户请求信息
     * @return 如果有用户登录，则返回当前用户信息，否则返回null
     * @author 郭欣光
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = null;
        if (session.getAttribute("user") != null) {
            user = (User)session.getAttribute("user");
        }
        return user;
    }
}

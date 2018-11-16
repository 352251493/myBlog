package com.gxg.service.Impl;

import com.gxg.dao.UserDao;
import com.gxg.entities.User;
import com.gxg.service.UserService;
import com.gxg.utils.Md5;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 用于用户相关的业务处理类
 * @author 郭欣光
 * @date 2018/10/12 17:14
 */
@Service(value = "userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Value("${user.max.number}")
    private int userMaxNumber;

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

    /**
     * 用户退出
     *
     * @param request 用户请求
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String signOut(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "true";
        String content = "退出成功！";
        result.accumulate("status", status);
        result.accumulate("content", content);
        HttpSession session = request.getSession();
        session.setAttribute("user", null);
        return result.toString();
    }

    /**
     * 修改密码
     *
     * @param oldPassword       原密码
     * @param newPassword       新密码
     * @param repeatNewPassword 重复密码
     * @param request           用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public synchronized String resetPassword(String oldPassword, String newPassword, String repeatNewPassword, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "修改失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或登录过期，请刷新页面重新登陆后再次尝试！";
        } else if (oldPassword == null || "".equals(oldPassword) || oldPassword.length() == 0) {
            content = "原密码不能为空！";
        } else if (newPassword == null || "".equals(newPassword) || newPassword.length() == 0) {
            content = "新密码不能为空！";
        } else if (newPassword.equals(repeatNewPassword)) {
            User user = (User)session.getAttribute("user");
            if (user.getPassword().equals(Md5.md5(oldPassword))) {
                user.setPassword(Md5.md5(newPassword));
                try {
                    if (userDao.updateUser(user) == 0) {
                        content = "操作数据库失败！";
                        System.out.println("修改用户" + user.getId() + "密码" + user.getPassword() + "时操作数据库失败！");
                    } else {
                        status = "true";
                        content = "修改成功！";
                        session.setAttribute("user", null);
                    }
                } catch (Exception e) {
                    content = "操作数据库失败！";
                    System.out.println("修改用户" + user.getId() + "密码" + user.getPassword() + "时操作数据库失败，失败原因：" + e);
                }
            } else {
                content = "密码错误！";
            }
        } else {
            content = "两次密码不一致！";
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 添加用户
     *
     * @param addUserId             用户账号
     * @param addUserPassword       密码
     * @param addUserRepeatPassword 重复密码
     * @param request               用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public synchronized String addUser(String addUserId, String addUserPassword, String addUserRepeatPassword, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "添加失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或登录过期，请刷新页面重新登陆后再次尝试！";
        } else if (!"管理员".equals(((User)session.getAttribute("user")).getRole())) {
            content = "仅有博客管理员可以添加编辑！";
        } else if (userDao.getCount() >= userMaxNumber) {
            content = "最多可以添加" + userMaxNumber + "个用户，现已超出限制！";
        } else if (addUserId == null || addUserId.length() == 0 || "".equals(addUserId)) {
            content = "账号不能为空！";
        } else if (addUserPassword == null || addUserPassword.length() == 0 || "".equals(addUserPassword)) {
            content = "密码不能为空！";
        } else if (addUserId.length() > 8) {
            content = "账号长度不能超过8个字符！";
        } else if (addUserPassword.equals(addUserRepeatPassword)) {
            if (userDao.getCountById(addUserId) == 0) {
                User user = new User();
                user.setId(addUserId);
                user.setPassword(Md5.md5(addUserPassword));
                user.setRole("编辑");
                try {
                    if (userDao.addUser(user) == 0) {
                        content = "操作数据库失败！";
                        System.out.println("添加用户" + user.toString() + "时操作数据库失败！");
                    } else {
                        status = "true";
                        content = "添加成功！";
                    }
                } catch (Exception e) {
                    content = "操作数据库失败！";
                    System.out.println("添加用户" + user.toString() + "时操作数据库失败，失败原因：" + e);
                }
            } else {
                content = "用户" + addUserId + "已存在！";
            }
        } else {
            content = "两次密码不一致！";
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 根据角色获取用户列表
     *
     * @param role    角色
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String getUserListByRole(String role, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        HttpSession session = request.getSession();
        String status = "false";
        String content = "获取失败！";
        List<User> userList = null;
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或登录过期，请刷新页面重新登陆后再次尝试！";
        } else {
            User user = (User)session.getAttribute("user");
            if (!"管理员".equals(user.getRole())) {
                content = "仅有博客管理员可以管理编辑！";
            } else if (userDao.getCountByRole(role) == 0) {
                content = "没有编辑信息！";
            } else {
                status = "true";
                userList = userDao.getUserByRole(role);
            }
        }
        result.accumulate("status", status);
        if (userList == null) {
            result.accumulate("content", content);
        } else {
            result.accumulate("content", userList);
        }
        return result.toString();
    }

    /**
     * 删除用户信息
     *
     * @param userId  用户账号
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public synchronized String deleteUser(String userId, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或登录过期，请刷新页面重新登陆后再次尝试！";
        } else {
            User user = (User)session.getAttribute("user");
            if (!"管理员".equals(user.getRole())) {
                content = "只有管理员才可以删除用户！";
            } else if (userDao.getCountById(userId) == 0) {
                content = "该用户不存在！";
            } else {
                try {
                    if (userDao.deleteUser(userId) == 0) {
                        content = "操作数据库失败！";
                        System.out.println("删除用户" + userId + "时，操作数据库失败！");
                    } else {
                        status = "true";
                        content = "删除成功！";
                    }
                } catch (Exception e) {
                    content = "操作数据库失败！";
                    System.out.println("删除用户" + userId + "时，操作数据库失败，失败原因：" + e);
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }
}

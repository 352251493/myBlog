package com.gxg.controller;

import com.gxg.service.CheckCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用于接收获取验证码的请求
 * @author 郭欣光
 * @date 2018/10/12 15:38
 */

@Controller
@RequestMapping(value = "/check_code")
public class CheckCodeController {

    @Autowired
    private CheckCodeService checkCodeService;

    @RequestMapping(value = "/get_check_code")
    public void getCheckCode(HttpServletRequest request, HttpServletResponse response) {
        checkCodeService.createCheckCode(request, response);
    }
}

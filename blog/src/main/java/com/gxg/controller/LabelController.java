package com.gxg.controller;

import com.gxg.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 用于接收响应用户关于标签相关的请求
 * @author 郭欣光
 * @date 2018/10/16 16:39
 */

@Controller
@RequestMapping(value = "/label")
public class LabelController {

    @Autowired
    private BlogService blogService;

    @PostMapping(value = "/add")
    @ResponseBody
    public String add(@RequestParam String name, HttpServletRequest request) {
        return blogService.addLabel(name, request);
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public String delete(@RequestParam String[] labelIdList, HttpServletRequest request) {
        return blogService.deleteLabel(labelIdList, request);
    }
}

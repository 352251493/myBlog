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
 * @author 郭欣光
 * @date 2018/10/15 15:04
 */

@Controller
@RequestMapping(value = "/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @PostMapping(value = "/set_base_information")
    @ResponseBody
    public String setBaseInformation(@RequestParam String ownerName, @RequestParam String ownerIntroduction, @RequestParam String ownerGithub, @RequestParam String ownerEmail, HttpServletRequest request) {
        return blogService.setBaseInformation(ownerName, ownerIntroduction, ownerGithub, ownerEmail, request);
    }
}

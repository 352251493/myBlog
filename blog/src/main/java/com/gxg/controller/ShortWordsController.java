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
 * 负责接收毒鸡汤相关的请求并给出响应
 * @author 郭欣光
 * @date 2018/10/18 10:50
 */

@Controller
@RequestMapping(value = "/short_words")
public class ShortWordsController {

    @Autowired
    private BlogService blogService;

    @PostMapping(value = "/add")
    @ResponseBody
    public String add(@RequestParam String word, HttpServletRequest request) {
        return blogService.addShortWords(word, request);
    }

    @PostMapping("/delete")
    @ResponseBody
    public String delete(@RequestParam String id, HttpServletRequest request) {
        return blogService.deleteShortWords(id, request);
    }
}

package com.gxg.controller;

import com.gxg.entities.Blog;
import com.gxg.entities.Image;
import com.gxg.entities.User;
import com.gxg.service.BlogService;
import com.gxg.service.ImageService;
import com.gxg.service.UserService;
import com.sun.javafx.sg.prism.NGShape;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 图片美景相关请求控制
 * @author 郭欣光
 * @date 2018/11/8 14:32
 */

@Controller
@RequestMapping(value = "/image/")
public class ImageController {

    @Autowired
    private UserService userService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private ImageService imageService;

    @GetMapping(value = "")
    public String image(Model model, HttpServletRequest request) {
        model.addAttribute("pageName", "图片美景");
        User user = userService.getLoginUser(request);
        if (user != null) {
            model.addAttribute("user", user);
        }
        Blog blog = blogService.getBlog(request);
        if (blog != null) {
            model.addAttribute("blog", blog);
        }
        List<Image> imageList = imageService.getImage();
        model.addAttribute("imageList", imageList);
        return "/image.html";
    }

    @PostMapping(value = "/upload")
    @ResponseBody
    public String upload(@RequestParam String imageTitle, @RequestParam MultipartFile uploadImage, HttpServletRequest request) {
        return imageService.uploadImage(imageTitle, uploadImage, request);
    }

    @GetMapping(value = "/manage")
    public String manage(Model model, HttpServletRequest request) {
        model.addAttribute("pageName", "图片美景");
        Blog blog = blogService.getBlog(request);
        if (blog != null) {
            model.addAttribute("blog", blog);
        }
        User user = userService.getLoginUser(request);
        if (user == null) {
            return "/error/no_user_login.html";
        }
        model.addAttribute("user", user);
        List<Image> imageList = imageService.getImage();
        model.addAttribute("imageList", imageList);
        return "/image_manage.html";
    }
}

package com.gxg.controller;

import com.gxg.entities.Blog;
import com.gxg.entities.Image;
import com.gxg.entities.User;
import com.gxg.service.BlogService;
import com.gxg.service.ImageService;
import com.gxg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}

package com.gxg.service.Impl;

import com.gxg.dao.BlogDao;
import com.gxg.entities.Blog;
import com.gxg.service.BlogService;
import com.gxg.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 提供博客信息相关的逻辑处理
 * @author 郭欣光
 * @date 2018/10/15 15:10
 */
@Service(value = "blogService")
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogDao blogDao;

    @Value("${blog.logo.ico.dir}")
    private String blogLogoIcoDir;

    @Value("${blog.logo.ico.name}")
    private String blogLogoIcoName;

    @Value("${blog.logo.dir}")
    private String blogLogoDir;

    @Value("${blog.logo.name}")
    private String blogLogoName;

    @Value("${blog,owner.head.img.dir}")
    private String blogOwnerHeadImgDir;

    @Value("${blog.owner.head.img.name}")
    private String blogOwnerHeadImgName;

    @Value("${blog,owner.qq.img.dir}")
    private String blogOwnerQqImgDir;

    @Value("${blog.owner.qq.img.name}")
    private String blogOwnerQqImgName;

    @Value("${blog.owner.weibo.img.dir}")
    private String blogOwnerWeiboImgDir;

    @Value("${blog.owner.weibo.img.name}")
    private String blogOwnerWeiboImgName;

    @Value("${blog.owner.weixin.img.dir}")
    private String blogOwnerWeixinImgDir;

    @Value("${blog.owner.weixin.img.name}")
    private String blogOwnerWeixinImgName;

    @Value("${blog.base.dir}")
    private String blogBaseDir;

    /**
     * 获得博客相关信息
     * @param request 用户请求信息
     * @return 博客相关信息
     * @author 郭欣光
     */
    @Override
    public Blog getBlog(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Blog blog = null;
        if (session.getAttribute("blog") == null) {
            if (blogDao.getBlogNumber() != 0) {
                blog = blogDao.getBlog();
                if (blog.getLogo() != null) {
                    String fileType = blog.getLogo().substring(blog.getLogo().lastIndexOf(".") + 1);
                    if (blogLogoDir.lastIndexOf("/") != blogLogoDir.length() - 1) {
                        blogLogoDir += "/";
                    }
                    if (FileUtil.fileExists(blogLogoDir + blogLogoName + "." + fileType)) {
                        if (blogBaseDir.lastIndexOf("/") != blogBaseDir.length() - 1) {
                            blogBaseDir += "/";
                        }
                        if (blogLogoDir.length() >= blogBaseDir.length()) {
                            String logoSrc = blogLogoDir.substring(blogBaseDir.length() -  1) + blogLogoName + "." + fileType;
                            blog.setLogo(logoSrc);
                        } else {
                            blog.setLogo(null);
                        }
                    } else {
                        blog.setLogo(null);
                    }
                }
                if (blog.getLogoIco() != null) {
                    String fileType = blog.getLogoIco().substring(blog.getLogoIco().lastIndexOf(".") + 1);
                    if (blogLogoIcoDir.lastIndexOf("/") != blogLogoIcoDir.length() - 1) {
                        blogLogoIcoDir += "/";
                    }
                    if (FileUtil.fileExists(blogLogoIcoDir + blogLogoIcoName + "." + fileType)) {
                        if (blogBaseDir.lastIndexOf("/") != blogBaseDir.length() - 1) {
                            blogBaseDir += "/";
                        }
                        if (blogLogoIcoDir.length() >= blogBaseDir.length()) {
                            String logoIcoSrc = blogLogoIcoDir.substring(blogBaseDir.length() -  1) + blogLogoIcoName + "." + fileType;
                            blog.setLogoIco(logoIcoSrc);
                        } else {
                            blog.setLogoIco(null);
                        }
                    } else {
                        blog.setLogoIco(null);
                    }
                }
                if (blog.getOwnerHeadImg() != null) {
                    String fileType = blog.getOwnerHeadImg().substring(blog.getOwnerHeadImg().lastIndexOf(".") + 1);
                    if (blogOwnerHeadImgDir.lastIndexOf("/") != blogOwnerHeadImgDir.length() - 1) {
                        blogOwnerHeadImgDir += "/";
                    }
                    if (FileUtil.fileExists(blogOwnerHeadImgDir + blogOwnerHeadImgName + "." + fileType)) {
                        if (blogBaseDir.lastIndexOf("/") != blogBaseDir.length() - 1) {
                            blogBaseDir += "/";
                        }
                        if (blogOwnerHeadImgDir.length() >= blogBaseDir.length()) {
                            String ownerHeadImgSrc = blogOwnerHeadImgDir.substring(blogBaseDir.length() -  1) + blogOwnerHeadImgName + "." + fileType;
                            blog.setOwnerHeadImg(ownerHeadImgSrc);
                        } else {
                            blog.setOwnerHeadImg(null);
                        }
                    } else {
                        blog.setOwnerHeadImg(null);
                    }
                }
                if (blog.getOwnerQqImg() != null) {
                    String fileType = blog.getOwnerQqImg().substring(blog.getOwnerQqImg().lastIndexOf(".") + 1);
                    if (blogOwnerQqImgDir.lastIndexOf("/") != blogOwnerQqImgDir.length() - 1) {
                        blogOwnerQqImgDir += "/";
                    }
                    if (FileUtil.fileExists(blogOwnerQqImgDir + blogOwnerQqImgName + "." + fileType)) {
                        if (blogBaseDir.lastIndexOf("/") != blogBaseDir.length() - 1) {
                            blogBaseDir += "/";
                        }
                        if (blogOwnerQqImgDir.length() >= blogBaseDir.length()) {
                            String ownerQqImgSrc = blogOwnerQqImgDir.substring(blogBaseDir.length() -  1) + blogOwnerQqImgName + "." + fileType;
                            blog.setOwnerQqImg(ownerQqImgSrc);
                        } else {
                            blog.setOwnerQqImg(null);
                        }
                    } else {
                        blog.setOwnerQqImg(null);
                    }
                }
                if (blog.getOwnerWeiboImg() != null) {
                    String fileType = blog.getOwnerWeiboImg().substring(blog.getOwnerWeiboImg().lastIndexOf(".") + 1);
                    if (blogOwnerWeiboImgDir.lastIndexOf("/") != blogOwnerWeiboImgDir.length() - 1) {
                        blogOwnerWeiboImgDir += "/";
                    }
                    if (FileUtil.fileExists(blogOwnerWeiboImgDir + blogOwnerWeiboImgName + "." + fileType)) {
                        if (blogBaseDir.lastIndexOf("/") != blogBaseDir.length() - 1) {
                            blogBaseDir += "/";
                        }
                        if (blogOwnerWeiboImgDir.length() >= blogBaseDir.length()) {
                            String ownerWeiboImgSrc = blogOwnerWeiboImgDir.substring(blogBaseDir.length() -  1) + blogOwnerWeiboImgName + "." + fileType;
                            blog.setOwnerWeiboImg(ownerWeiboImgSrc);
                        } else {
                            blog.setOwnerWeiboImg(null);
                        }
                    } else {
                        blog.setOwnerWeiboImg(null);
                    }
                }
                if (blog.getOwnerWeixinImg() != null) {
                    String fileType = blog.getOwnerWeixinImg().substring(blog.getOwnerWeixinImg().lastIndexOf(".") + 1);
                    if (blogOwnerWeixinImgDir.lastIndexOf("/") != blogOwnerWeixinImgDir.length() - 1) {
                        blogOwnerWeixinImgDir += "/";
                    }
                    if (FileUtil.fileExists(blogOwnerWeixinImgDir + blogOwnerWeixinImgName + "." + fileType)) {
                        if (blogBaseDir.lastIndexOf("/") != blogBaseDir.length() - 1) {
                            blogBaseDir += "/";
                        }
                        if (blogOwnerWeixinImgDir.length() >= blogBaseDir.length()) {
                            String ownerWeixinImgSrc = blogOwnerWeixinImgDir.substring(blogBaseDir.length() -  1) + blogOwnerWeixinImgName + "." + fileType;
                            blog.setOwnerWeixinImg(ownerWeixinImgSrc);
                        } else {
                            blog.setOwnerWeixinImg(null);
                        }
                    } else {
                        blog.setOwnerWeixinImg(null);
                    }
                }
                session.setAttribute("blog", blog);
            }
        } else {
            blog = (Blog)session.getAttribute("blog");
        }
        return blog;
    }
}

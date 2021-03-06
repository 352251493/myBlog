package com.gxg.service.Impl;

import com.gxg.dao.BlogDao;
import com.gxg.dao.LabelDao;
import com.gxg.dao.ShortWordsDao;
import com.gxg.entities.Blog;
import com.gxg.entities.Label;
import com.gxg.entities.ShortWords;
import com.gxg.service.BlogService;
import com.gxg.utils.FileUtil;
import com.gxg.utils.RegularExpressionUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

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

    @Autowired
    private LabelDao labelDao;

    @Value("${blog.label.count}")
    private int blogLabelCount;

    @Autowired
    private ShortWordsDao shortWordsDao;

    @Value("${blog,shortWords.count}")
    private int blogShortWordsCount;

    private final String blogInformationBaseUrl = "/blog_resource";

    /**
     * 获得博客相关信息
     * @param request 用户请求信息
     * @return 博客相关信息
     * @author 郭欣光
     */
    @Override
    public Blog getBlog(HttpServletRequest request) {
        ServletContext servletContext = request.getServletContext();
//        HttpSession session = request.getSession();
        Blog blog = null;
        Random random = new Random();
        if (servletContext.getAttribute("blog") == null) {
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
                            String logoSrc = blogInformationBaseUrl + blogLogoDir.substring(blogBaseDir.length() -  1) + blogLogoName + "." + fileType + "?noCache=" + random.nextDouble();
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
                            String logoIcoSrc = blogInformationBaseUrl + blogLogoIcoDir.substring(blogBaseDir.length() -  1) + blogLogoIcoName + "." + fileType + "?noCache=" + random.nextDouble();
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
                            String ownerHeadImgSrc = blogInformationBaseUrl + blogOwnerHeadImgDir.substring(blogBaseDir.length() -  1) + blogOwnerHeadImgName + "." + fileType + "?noCache=" + random.nextDouble();
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
                            String ownerQqImgSrc = blogInformationBaseUrl + blogOwnerQqImgDir.substring(blogBaseDir.length() -  1) + blogOwnerQqImgName + "." + fileType + "?noCache=" + random.nextDouble();
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
                            String ownerWeiboImgSrc = blogInformationBaseUrl + blogOwnerWeiboImgDir.substring(blogBaseDir.length() -  1) + blogOwnerWeiboImgName + "." + fileType + "?noCache=" + random.nextDouble();
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
                            String ownerWeixinImgSrc = blogInformationBaseUrl + blogOwnerWeixinImgDir.substring(blogBaseDir.length() -  1) + blogOwnerWeixinImgName + "." + fileType + "?noCache=" + random.nextDouble();
                            blog.setOwnerWeixinImg(ownerWeixinImgSrc);
                        } else {
                            blog.setOwnerWeixinImg(null);
                        }
                    } else {
                        blog.setOwnerWeixinImg(null);
                    }
                }
                servletContext.setAttribute("blog", blog);
            }
        } else {
            blog = (Blog)servletContext.getAttribute("blog");
        }
        return blog;
    }

    /**
     * 获得个人博客标签
     *
     * @param request 用户请求信息
     * @return List<Label>个人博客标签
     * @author 郭欣光
     */
    @Override
    public List<Label> getLabel(HttpServletRequest request) {
        ServletContext servletContext = request.getServletContext();
        List<Label> labelList = null;
        if (servletContext.getAttribute("label") == null) {
            if (labelDao.getCount() != 0) {
                labelList = labelDao.getLabelByLimitOrderByTime(0, blogLabelCount);
                servletContext.setAttribute("label", labelList);
            }
        } else {
            labelList = (List<Label>) servletContext.getAttribute("label");
        }
        return labelList;
    }

    /**
     * 获取系统保存Label的最大个数
     *
     * @return 返回系统保存Label的最大个数
     * @author 郭欣光
     */
    @Override
    public int getSaveLabelCount() {
        return blogLabelCount;
    }

    /**
     * 获得毒鸡汤信息
     *
     * @param request 用户请求信息
     * @return 毒鸡汤信息
     * @author 郭欣光
     */
    @Override
    public List<ShortWords> getShortWords(HttpServletRequest request) {
        ServletContext servletContext = request.getServletContext();
        List<ShortWords> shortWordsList = null;
        if (servletContext.getAttribute("shortWords") == null) {
            if (shortWordsDao.getCount() != 0) {
                shortWordsList = shortWordsDao.getShortWordsByLimitOrderByTime(0, blogShortWordsCount);
                servletContext.setAttribute("shortWords", shortWordsList);
            }
        } else {
            shortWordsList = (List<ShortWords>)servletContext.getAttribute("shortWords");
        }
        return shortWordsList;
    }

    /**
     * 获得系统保存毒鸡汤的最大个数
     *
     * @return 系统保存毒鸡汤的最大个数
     * @author 郭欣光
     */
    @Override
    public int getSaveShortWordsCount() {
        return blogShortWordsCount;
    }

    /**
     * 添加标签
     *
     * @param name    标签名称
     * @param request 用户请求信息
     * @return 添加处理结果
     * @author 郭欣光
     */
    @Override
    public synchronized String addLabel(String name, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "添加失败！";
        HttpSession session = request.getSession();
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或登录过期，请刷新页面重新登陆后再次尝试！";
        } else if (name == null || name.length() == 0 || "".equals(name)) {
            content = "标签名称不能为空！";
        } else if (name.length() > 30) {
            content = "标签名称长度不能超过30字符！";
        } else if (labelDao.getCount() >= blogLabelCount) {
            content = "系统设置标签最大个数为" + blogLabelCount + "个，请删除后再次尝试！";
        } else {
            Label label = new Label();
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String timeString = time.toString();
            String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
            while (labelDao.getCountById(id) != 0) {
                long idLong = Long.parseLong(id);
                Random random = new Random();
                idLong += random.nextInt(100);
                id = idLong + "";
                if (id.length() > 17) {
                    id = id.substring(0, 17);
                }
            }
            label.setId(id);
            label.setName(name);
            label.setTime(time);
            boolean isAddSuccess = false;
            try {
                if (labelDao.addLabel(label) == 0) {
                    content = "添加数据库失败！";
                } else {
                    ServletContext servletContext = request.getServletContext();
                    isAddSuccess = true;
                    status = "true";
                    content = "添加成功！";
                    servletContext.setAttribute("label", null);
                }
            } catch (Exception e) {
                content = "添加数据库失败！";
            }
            if (isAddSuccess && labelDao.getCount() > blogLabelCount) {
                try {
                    Label deleteLabel = labelDao.getLabelByLimitOrderByTimeAsc(0, 1).get(0);
                    if (labelDao.deleteLabelById(deleteLabel.getId()) == 0) {
                        System.out.println("在添加标签时删除超过数量的标签失败！");
                    }
                } catch (Exception e) {
                    System.out.println("在添加标签时删除超过数量的标签失败！");
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 删除标签
     *
     * @param labelIdList 要删除的标签ID列表
     * @param request     用户请求的内容
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public synchronized String deleteLabel(String[] labelIdList, HttpServletRequest request) {
        JSONObject result = new JSONObject();
        HttpSession session = request.getSession();
        String status = "false";
        String content = "删除失败！";
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或者用户登录信息过期，请刷新页面重新登陆后再次尝试！";
        } else if (labelIdList == null || labelIdList.length == 0) {
            content = "请选择要删除的标签！";
        } else {
            try {
                labelDao.delete(labelIdList);
                status = "true";
                content = "删除成功！";
                ServletContext servletContext = request.getServletContext();
                servletContext.setAttribute("label", null);
            } catch (Exception e) {
                content = "从数据库删除失败！";
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 添加毒鸡汤
     *
     * @param word    毒鸡汤
     * @param request 用户请求信息
     * @return String 处理结果
     * @author 郭欣光
     */
    @Override
    public synchronized String addShortWords(String word, HttpServletRequest request) {
        HttpSession session = request.getSession();
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "添加失败！";
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或者用户登录信息过期，请刷新页面重新登陆后再次尝试！";
        } else if (word == null || word.length() == 0 || "".equals(word)) {
            content = "毒鸡汤不能为空！";
        } else if (word.length() > 100) {
            content = "毒鸡汤长度不能超过100字符！";
        } else if (shortWordsDao.getCount() >= blogShortWordsCount) {
            content = "系统设置标签最大个数为" + blogShortWordsCount + "个，请删除后再次尝试！";
        } else {
            ShortWords shortWords = new ShortWords();
            Timestamp time = new Timestamp(System.currentTimeMillis());
            String timeString = time.toString();
            String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
            while (shortWordsDao.getCountById(id) != 0) {
                long idLong = Long.parseLong(id);
                Random random = new Random();
                idLong += random.nextInt(100);
                id = idLong + "";
                if (id.length() > 17) {
                    id = id.substring(0, 17);
                }
            }
            shortWords.setId(id);
            shortWords.setWord(word);
            shortWords.setTime(time);
            boolean isAddSuccess = false;
            try {
                if (shortWordsDao.addShortWords(shortWords) == 0) {
                    content = "毒鸡汤添加数据库失败！";
                } else {
                    isAddSuccess = true;
                    ServletContext servletContext = request.getServletContext();
                    servletContext.setAttribute("shortWords", null);
                    status = "true";
                    content = "添加成功！";
                }
            } catch (Exception e) {
                content = "毒鸡汤添加数据库失败！";
            }

            if (isAddSuccess && shortWordsDao.getCount() > blogShortWordsCount) {
                try {
                    ShortWords deleteShortWords = shortWordsDao.getShortWordsByLimitOrderByTimeAsc(0, 1).get(0);
                    if (shortWordsDao.deleteShortWordsById(deleteShortWords.getId()) == 0) {
                        System.out.println("在添加毒鸡汤时删除超过数量的毒鸡汤失败！");
                    }
                } catch (Exception e) {
                    System.out.println("在添加毒鸡汤时删除超过数量的毒鸡汤失败！");
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 删除指定id的毒鸡汤
     *
     * @param id      毒鸡汤id
     * @param request 用户请求内容
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String deleteShortWords(String id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "删除失败！";
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或者用户登录信息过期，请刷新页面重新登陆后再次尝试！";
        } else if (id == null || id.length() == 0 || "".equals(id)) {
            content = "请选择要删除的毒鸡汤！";
        } else {
            try {
                if (shortWordsDao.deleteShortWordsById(id) == 0) {
                    content = "从数据库删除失败！";
                } else {
                    status = "true";
                    content = "删除成功！";
                    ServletContext servletContext = request.getServletContext();
                    servletContext.setAttribute("shortWords", null);
                }
            } catch (Exception e) {
                content = "从数据库删除失败！";
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 设置博客基础信息
     *
     * @param ownerName         博客所属者昵称
     * @param ownerIntroduction 博客所属这简介
     * @param ownerGithub       博客所属者Github
     * @param ownerEmail        博客所属者Email
     * @param request           用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String setBaseInformation(String ownerName, String ownerIntroduction, String ownerGithub, String ownerEmail, HttpServletRequest request) {
        HttpSession session = request.getSession();
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "设置失败！";
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或者用户登录信息过期，请刷新页面重新登陆后再次尝试！";
        } else if (ownerName == null || ownerName.length() == 0 || "".equals(ownerName)) {
            content = "昵称不能为空！";
        } else if (ownerIntroduction == null || ownerIntroduction.length() == 0 || "".equals(ownerIntroduction)) {
            content = "简介不能为空！";
        } else if (ownerGithub == null || ownerGithub.length() == 0 || "".equals(ownerGithub)) {
            content = "Github不能为空！";
        } else if (ownerEmail == null || ownerEmail.length() == 0 || "".equals(ownerEmail)) {
            content = "邮箱不能为空！";
        } else if (ownerName.length() > 10) {
            content = "昵称长度不能超过10字符！";
        } else if (ownerIntroduction.length() > 100) {
            content = "简介长度不能超过100字符！";
        } else if (ownerGithub.length() > 100) {
            content = "Github长度不能超过100字符！";
        } else if (!RegularExpressionUtil.checkUrl(ownerGithub)) {
            content = "Github不是一个标准url格式！";
        } else if (ownerEmail.length() > 100) {
            content = "邮箱长度不能超过100字符！";
        } else if (!RegularExpressionUtil.checkEmail(ownerEmail)) {
            content = "邮箱不是一个标准Email格式！";
        } else {
            Blog blog;
            boolean isNew = false;
            String oldBlogName = null;
            if (blogDao.getBlogNumber() == 0) {
                isNew = true;
                blog = new Blog();
            } else {
                blog = blogDao.getBlog();
                oldBlogName = blog.getOwnerName();
            }
            blog.setOwnerName(ownerName);
            blog.setOwnerIntroduction(ownerIntroduction);
            blog.setOwnerGithub(ownerGithub);
            blog.setOwnerEmail(ownerEmail);
            ServletContext servletContext = request.getServletContext();
            if (isNew) {
                try {
                    if (blogDao.insertBlog(blog) == 0) {
                        content = "博客信息写入数据库失败！";
                    } else {
                        servletContext.setAttribute("blog", null);
                        status = "true";
                        content = "设置成功！";
                    }
                } catch (Exception e) {
                    content = "博客信息写入数据库失败！";
                }
            } else {
                try {
                    if (blogDao.updateBlogByOwnerName(blog, oldBlogName) == 0) {
                        content = "博客信息写入数据库失败！";
                    } else {
                        servletContext.setAttribute("blog", null);
                        status = "true";
                        content = "设置成功！";
                    }
                } catch (Exception e) {
                    content = "博客信息写入数据库失败！";
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    /**
     * 设置博客其他信息业务处理
     *
     * @param informationName 其他信息名称
     * @param uploadBlogImage 图片
     * @param request         用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public String setOtherInformation(String informationName, MultipartFile uploadBlogImage, HttpServletRequest request) {
        HttpSession session = request.getSession();
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "设置失败！";
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或者用户登录信息过期，请刷新页面重新登陆后再次尝试！";
        } else if (blogDao.getBlogNumber() == 0) {
            content = "您的博客基础信息尚未设置，请先设置博客基础信息后再设置其他信息！";
        } else if (informationName == null || informationName.length() == 0 || "".equals(informationName)) {
            content = "请求不符合要求！";
        } else if (!"logo-ico".equals(informationName) && !"logo".equals(informationName) && !"head-img".equals(informationName) && !"my-qq".equals(informationName) && !"my-weibo".equals(informationName) && !"my-weixin".equals(informationName)) {
            content = "请求不符合要求！";
        } else if (uploadBlogImage == null) {
            content = "请选择要上传的图片！";
        } else {
            boolean isImage = true;
            try {
                if (!FileUtil.isImage(uploadBlogImage)) {
                    isImage = false;
                    content = "上传的文件必须是图片类型！";
                }
            } catch (Exception e) {
                isImage = false;
                content = "判断文件是否合法时系统异常！";
            }
            if (isImage) {
                String uploadBlogImageName = uploadBlogImage.getOriginalFilename();//上传图片的名称
                String uploadBlogImageType = uploadBlogImageName.substring(uploadBlogImageName.lastIndexOf(".") + 1);//上传图片的后缀类型
                if (!FileUtil.isImageByType(uploadBlogImageType)) {
                    content = "上传的文件必须是图片类型！";
                } else if (FileUtil.getFileSize(uploadBlogImage) > 50 * 1024 * 1024) {
                    content = "图片大小不能超过50MB！";
                } else{
                    Blog blog = blogDao.getBlog();
                    String uploadUrl = null;
                    String uploadImageName = null;
                    String oldImageName = null;
                    if (blogLogoDir.lastIndexOf("/") != blogLogoDir.length() - 1) {
                        blogLogoDir += "/";
                    }
                    if (blogLogoIcoDir.lastIndexOf("/") != blogLogoIcoDir.length() - 1) {
                        blogLogoIcoDir += "/";
                    }
                    if (blogOwnerHeadImgDir.lastIndexOf("/") != blogOwnerHeadImgDir.length() - 1) {
                        blogOwnerHeadImgDir += "/";
                    }
                    if (blogOwnerQqImgDir.lastIndexOf("/") != blogOwnerQqImgDir.length() - 1) {
                        blogOwnerQqImgDir += "/";
                    }
                    if (blogOwnerWeiboImgDir.lastIndexOf("/") != blogOwnerWeiboImgDir.length() - 1) {
                        blogOwnerWeiboImgDir += "/";
                    }
                    if (blogOwnerWeixinImgDir.lastIndexOf("/") != blogOwnerWeixinImgDir.length() - 1) {
                        blogOwnerWeixinImgDir += "/";
                    }
                    switch (informationName) {
                        case "logo-ico":
                            oldImageName = blog.getLogoIco();
                            uploadUrl = blogLogoIcoDir;
                            uploadImageName = blogLogoIcoName;
                            blog.setLogoIco(uploadImageName + "." + uploadBlogImageType);
                            break;
                        case "logo":
                            oldImageName = blog.getLogo();
                            uploadUrl = blogLogoDir;
                            uploadImageName = blogLogoName;
                            blog.setLogo(uploadImageName + "." + uploadBlogImageType);
                            break;
                        case "head-img":
                            oldImageName = blog.getOwnerHeadImg();
                            uploadUrl = blogOwnerHeadImgDir;
                            uploadImageName = blogOwnerHeadImgName;
                            blog.setOwnerHeadImg(uploadImageName + "." + uploadBlogImageType);
                            break;
                        case "my-qq":
                            oldImageName = blog.getOwnerQqImg();
                            uploadUrl = blogOwnerQqImgDir;
                            uploadImageName = blogOwnerQqImgName;
                            blog.setOwnerQqImg(uploadImageName + "." + uploadBlogImageType);
                            break;
                        case "my-weibo":
                            oldImageName = blog.getOwnerWeiboImg();
                            uploadUrl = blogOwnerWeiboImgDir;
                            uploadImageName = blogOwnerWeiboImgName;
                            blog.setOwnerWeiboImg(uploadImageName + "." + uploadBlogImageType);
                            break;
                        case "my-weixin":
                            oldImageName = blog.getOwnerWeixinImg();
                            uploadUrl = blogOwnerWeixinImgDir;
                            uploadImageName = blogOwnerWeixinImgName;
                            blog.setOwnerWeixinImg(uploadImageName + "." + uploadBlogImageType);
                            break;
                    }
                    if (uploadImageName == null || uploadUrl == null) {
                        content = "请求不符合要求！";
                    } else {
                        JSONObject uploadFileResult = FileUtil.uploadFile(uploadBlogImage, uploadImageName + "." + uploadBlogImageType, uploadUrl);
                        if ("false".equals(uploadFileResult.getString("status"))) {
                            content = "设置其他信息出错，错误原因：" + uploadFileResult.getString("content");
                        } else {
                            ServletContext servletContext = request.getServletContext();
                            servletContext.setAttribute("blog", null);
                            if (oldImageName != null && (uploadImageName + "." + uploadBlogImageType).toLowerCase().equals(oldImageName.toLowerCase())) {
                                status = "true";
                                content = "设置成功！";
                            } else {
                                try {
                                    blogDao.updateBlogByOwnerName(blog, blog.getOwnerName());
                                    status = "true";
                                    content = "设置成功！";
                                } catch (Exception e) {
                                    content = "设置失败，操作数据库失败！";
                                }
                                if ("true".equals(status)) {
                                    if (oldImageName != null) {
                                        System.out.println("删除原图片结果：" + FileUtil.deleteFile(uploadUrl + oldImageName).toString());
                                    }
                                } else {
                                    System.out.println("删除刚上传图片结果：" + FileUtil.deleteFile(uploadUrl + uploadImageName + "." + uploadBlogImageType).toString());
                                }
                            }
                        }
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }
}

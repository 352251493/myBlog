package com.gxg.service.Impl;

import com.gxg.dao.ImageDao;
import com.gxg.entities.Article;
import com.gxg.entities.Image;
import com.gxg.service.ImageService;
import com.gxg.utils.FileUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * 图片美景相关处理类
 * @author 郭欣光
 * @date 2018/11/8 14:51
 */

@Service(value = "/imageServer")
public class ImageServerImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Value("${image.max.number}")
    private int imageMaxNumber;

    @Value("${blog.base.dir}")
    private String blogBaseDir;

    @Value("${image.dir}")
    private String imageDir;

    private final String blogInformationBaseUrl = "/blog_resource";

    /**
     * 获取图片信息
     *
     * @return 图片信息
     * @author 郭欣光
     */
    @Override
    public List<Image> getImage() {
        if (imageDao.getCount() == 0) {
            return null;
        } else {
            List<Image> imageList = imageDao.getImageByLimitOrderByUploadTime(0, imageMaxNumber);
            baseDirProcess();
            imageList = this.imageListDirProcess(imageList);
            return imageList;
        }
    }

    /**
     * 上传图片
     * @param imageTitle 图片标题
     * @param uploadImage 图片
     * @param request 用户请求信息
     * @return 处理结果
     * @author 郭欣光
     */
    @Override
    public synchronized String uploadImage(String imageTitle, MultipartFile uploadImage, HttpServletRequest request) {
        HttpSession session = request.getSession();
        JSONObject result = new JSONObject();
        String status = "false";
        String content = "上传失败！";
        if (session.getAttribute("user") == null) {
            content = "用户未登录，或者用户登录信息过期，请刷新页面重新登陆后再次尝试！";
        } else if (imageDao.getCount() >= imageMaxNumber) {
            content = "图片美景图片超过限制，系统仅允许上传" + imageMaxNumber + "张图片！";
        } else if (imageTitle == null || imageTitle.length() == 0 || "".equals(imageTitle)) {
            content = "图片标题不能为空！";
        } else if (imageTitle.length() > 50) {
            content = "图片标题不能超过50字符！";
        } else if (uploadImage == null) {
            content = "请选择要上传的图片！";
        } else {
            boolean isImage = true;
            try {
                if (!FileUtil.isImage(uploadImage)) {
                    isImage = false;
                    content = "上传的文件必须是图片类型！";
                }
            } catch (Exception e) {
                isImage = false;
                System.out.println("在图片美景上传图片验证图片类型时出错，错误原因："  + e);
                content = "判断文件是否合法时系统异常！";
            }
            if (isImage) {
                String uploadImageName = uploadImage.getOriginalFilename();
                String uploadImageType = uploadImageName.substring(uploadImageName.lastIndexOf(".") + 1);//上传图片的后缀类型
                if (!FileUtil.isImageByType(uploadImageType)) {
                    content = "上传的文件必须是图片类型！";
                } else if (FileUtil.getFileSize(uploadImage) > 50 * 1024 * 1024) {
                    content = "图片大小不能超过50MB！";
                } else {
                    this.baseDirProcess();
                    Timestamp time = new Timestamp(System.currentTimeMillis());
                    String timeString = time.toString();
                    String id = timeString.split(" ")[0].split("-")[0] + timeString.split(" ")[0].split("-")[1] + timeString.split(" ")[0].split("-")[2] + timeString.split(" ")[1].split(":")[0] + timeString.split(" ")[1].split(":")[1] + timeString.split(" ")[1].split(":")[2].split("\\.")[0] + timeString.split(" ")[1].split(":")[2].split("\\.")[1];//注意，split是按照正则表达式进行分割，.在正则表达式中为特殊字符，需要转义。
                    while (imageDao.getCountById(id) != 0) {
                        long idLong = Long.parseLong(id);
                        Random random = new Random();
                        idLong += random.nextInt(100);
                        id = idLong + "";
                        if (id.length() > 17) {
                            id = id.substring(0, 17);
                        }
                    }
                    String img = id + "." + uploadImageType;
                    JSONObject uploadImageResult = FileUtil.uploadFile(uploadImage, img, imageDir);
                    if ("true".equals(uploadImageResult.getString("status"))) {
                        Image image = new Image();
                        image.setId(id);
                        image.setTitle(imageTitle);
                        image.setImg(img);
                        image.setUploadTime(time);
                        try {
                            if (imageDao.createImage(image) == 0) {
                                System.out.println("在图片美景上传图片时操作数据库失败！");
                                content = "操作数据库时失败！";
                            } else {
                                status = "true";
                                content = "上传成功！";
                            }
                        } catch (Exception e) {
                            System.out.println("在图片美景上传图片时操作数据库失败！失败原因：" + e);
                            content = "操作数据库失败！";
                        }
                        if ("false".equals(status)) {
                            JSONObject deleteImageResult = FileUtil.deleteFile(imageDir + img);
                            System.out.println("在图片美景上传图片时操作数据库失败后删除图片结果：" + deleteImageResult.toString());
                        }
                    } else {
                        System.out.println("在图片美景上传图片失败，失败原因：" + uploadImageResult.getString("content"));
                        content = "上传图片失败，失败原因：" + uploadImageResult.getString("content");
                    }
                }
            }
        }
        result.accumulate("status", status);
        result.accumulate("content", content);
        return result.toString();
    }

    private void baseDirProcess() {
        if (blogBaseDir.lastIndexOf("/") != blogBaseDir.length() - 1) {
            blogBaseDir += "/";
        }
        if (imageDir.lastIndexOf("/") != imageDir.length() - 1) {
            imageDir += "/";
        }
    }

    private Image singleImageDirProcess(Image image) {
        if (imageDir.length() >= blogBaseDir.length() && image.getImg() != null && FileUtil.fileExists(imageDir + image.getImg())) {
            Random random = new Random();
            String imgUrl = blogInformationBaseUrl + imageDir.substring(blogBaseDir.length() - 1) + image.getImg() + "?noCache=" + random.nextDouble();
            image.setImg(imgUrl);
        }
        return image;
    }

    private List<Image> imageListDirProcess(List<Image> imageList) {
        for (int i = 0; i < imageList.size(); i++) {
            Image image = imageList.get(i);
            image = this.singleImageDirProcess(image);
            imageList.set(i, image);
        }
        return imageList;
    }
}

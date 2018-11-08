package com.gxg.service.Impl;

import com.gxg.dao.ImageDao;
import com.gxg.entities.Article;
import com.gxg.entities.Image;
import com.gxg.service.ImageService;
import com.gxg.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
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

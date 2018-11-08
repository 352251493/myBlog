package com.gxg.service;

import com.gxg.entities.Image;
import org.springframework.aop.target.LazyInitTargetSource;

import java.util.List;

/**
 * 美景图片相关处理接口
 * @author 郭欣光
 * @date 2018/11/8 14:47
 */
public interface ImageService {

    /**
     * 获取图片信息
     * @return 图片信息
     * @author 郭欣光
     */
    public List<Image> getImage();
}

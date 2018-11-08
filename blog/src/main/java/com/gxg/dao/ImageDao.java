package com.gxg.dao;

import com.gxg.entities.Image;

import java.util.List;

/**
 * 图片美景相关数据库操作
 * @author 郭欣光
 * @date 2018/11/8 14:52
 */
public interface ImageDao {

    /**
     * 获取图片美景数据库数量
     * @return 图片美景数量
     * @author 郭欣光
     */
    public int getCount();

    /**
     * 获取按照上传时间排序的图片美景指定位置指定个数的图片美景信息
     * @param start 数据库开始位置
     * @param length 获取个数
     * @return 图片美景信息
     * @author 郭欣光
     */
    public List<Image> getImageByLimitOrderByUploadTime(int start, int length);
}

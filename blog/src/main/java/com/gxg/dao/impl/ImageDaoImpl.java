package com.gxg.dao.impl;

import com.gxg.dao.ImageDao;
import com.gxg.dao.rowmapper.ImageRowMapper;
import com.gxg.entities.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 图片美景数据库操作
 * @author 郭欣光
 * @date 2018/11/8 14:53
 */

@Repository(value = "imageDao")
public class ImageDaoImpl implements ImageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取图片美景数据库数量
     *
     * @return 图片美景数量
     * @author 郭欣光
     */
    @Override
    public int getCount() {
        String sql = "select count(1) from image";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    /**
     * 获取按照上传时间排序的图片美景指定位置指定个数的图片美景信息
     *
     * @param start  数据库开始位置
     * @param length 获取个数
     * @return 图片美景信息
     * @author 郭欣光
     */
    @Override
    public List<Image> getImageByLimitOrderByUploadTime(int start, int length) {
        String sql = "select * from image order by upload_time desc limit ?, ?";
        List<Image> imageList = jdbcTemplate.query(sql, new ImageRowMapper(), start, length);
        return imageList;
    }
}

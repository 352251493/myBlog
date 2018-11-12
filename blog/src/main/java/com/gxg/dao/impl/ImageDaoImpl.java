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

    /**
     * 获得指定图片美景ID的图片个数
     *
     * @param id 图片美景ID
     * @return 图片个数
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from image where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 创建图片美景信息
     *
     * @param image 图片美景信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int createImage(Image image) {
        String sql = "insert into image(id, title, img, upload_time) values(?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, image.getId(), image.getTitle(), image.getImg(), image.getUploadTime());
        return changeCount;
    }

    /**
     * 根据ID获得图片美景信息
     *
     * @param id 图片美景ID
     * @return 图片美景信息
     * @author 郭欣光
     */
    @Override
    public Image getImageById(String id) {
        String sql = "select * from image where id=?";
        Image image = jdbcTemplate.queryForObject(sql, new ImageRowMapper(), id);
        return image;
    }

    /**
     * 删除美景图片信息
     *
     * @param id 美景图片ID
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteImage(String id) {
        String sql = "delete from image where id=?";
        int changeCount = jdbcTemplate.update(sql, id);
        return changeCount;
    }
}

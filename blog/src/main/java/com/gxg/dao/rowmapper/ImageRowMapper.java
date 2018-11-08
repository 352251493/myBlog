package com.gxg.dao.rowmapper;

import com.gxg.entities.Image;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2018/11/8 15:01
 */
public class ImageRowMapper implements RowMapper<Image> {

    @Override
    public Image mapRow(ResultSet resultSet, int i) throws SQLException {
        Image image = new Image();
        image.setId(resultSet.getString("id"));
        image.setTitle(resultSet.getString("title"));
        image.setImg(resultSet.getString("img"));
        image.setUploadTime(resultSet.getTimestamp("upload_time"));
        return image;
    }
}

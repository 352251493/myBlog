package com.gxg.dao.rowmapper;

import com.gxg.entities.Label;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2018/10/16 10:59
 */
public class LabelRowMapper implements RowMapper<Label> {

    @Override
    public Label mapRow(ResultSet resultSet, int i) throws SQLException {
        Label label = new Label();
        label.setId(resultSet.getString("id"));
        label.setName(resultSet.getString("name"));
        label.setTime(resultSet.getTimestamp("time"));
        return label;
    }
}

package com.gxg.dao.rowmapper;

import com.gxg.entities.LeaveMessage;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author 郭欣光
 * @date 2018/11/13 14:40
 */
public class LeaveMessageMapper implements RowMapper<LeaveMessage> {


    @Override
    public LeaveMessage mapRow(ResultSet resultSet, int i) throws SQLException {
        LeaveMessage leaveMessage = new LeaveMessage();
        leaveMessage.setId(resultSet.getString("id"));
        leaveMessage.setHeadImg(resultSet.getString("head_img"));
        leaveMessage.setComment(resultSet.getString("comment"));
        leaveMessage.setName(resultSet.getString("name"));
        leaveMessage.setEmail(resultSet.getString("email"));
        leaveMessage.setCreateTime(resultSet.getTimestamp("create_time"));
        return leaveMessage;
    }
}

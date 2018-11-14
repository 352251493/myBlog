package com.gxg.dao.impl;

import com.gxg.dao.LeaveMessageDao;
import com.gxg.dao.rowmapper.LeaveMessageMapper;
import com.gxg.entities.LeaveMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 留言相关数据库操作
 * @author 郭欣光
 * @date 2018/11/13 14:26
 */
@Repository(value = "leaveMessageDao")
public class LeaveMessageDaoImpl implements LeaveMessageDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获得留言数量
     *
     * @return 留言数量
     * @author 郭欣光
     */
    @Override
    public int getCount() {
        String sql = "select count(1) from leave_message;";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class);
        return rowCount;
    }

    /**
     * 获取按创建时间排序的指定区间的留言信息
     *
     * @param start  数据库开始位置
     * @param length 获取个数
     * @return 留言列表
     * @author 郭欣光
     */
    @Override
    public List<LeaveMessage> getLeaveMessageByLimitOrderByCreateTime(int start, int length) {
        String sql = "select * from leave_message order by create_time desc limit ?, ?";
        List<LeaveMessage> leaveMessageList = jdbcTemplate.query(sql, new LeaveMessageMapper(), start, length);
        return leaveMessageList;
    }

    /**
     * 蝴蝶指定id的留言个数
     *
     * @param id 留言ID
     * @return 留言个数
     * @author 郭欣光
     */
    @Override
    public int getCountById(String id) {
        String sql = "select count(1) from leave_message where id=?";
        int rowCount = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return rowCount;
    }

    /**
     * 创建留言信息
     *
     * @param leaveMessage 留言信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int createLeaveMessage(LeaveMessage leaveMessage) {
        String sql = "insert into leave_message(id, head_img, comment, name, email, create_time) values(?, ?, ?, ?, ?, ?)";
        int changeCount = jdbcTemplate.update(sql, leaveMessage.getId(), leaveMessage.getHeadImg(), leaveMessage.getComment(), leaveMessage.getName(), leaveMessage.getEmail(), leaveMessage.getCreateTime());
        return changeCount;
    }

    /**
     * 删除留言
     *
     * @param id 留言ID
     * @return 数据库改变行数
     * @author 郭欣光
     */
    @Override
    public int deleteLeaveMessage(String id) {
        String sql = "delete from leave_message where id=?";
        int changeCount = jdbcTemplate.update(sql, id);
        return changeCount;
    }
}

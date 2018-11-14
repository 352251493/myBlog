package com.gxg.dao;

import com.gxg.entities.LeaveMessage;

import java.util.List;

/**
 * 留言与数据库操作
 * @author 郭欣光
 * @date 2018/11/13 14:25
 */
public interface LeaveMessageDao {

    /**
     * 获得留言数量
     * @return 留言数量
     * @author 郭欣光
     */
    public int getCount();

    /**
     * 获取按创建时间排序的指定区间的留言信息
     * @param start 数据库开始位置
     * @param length 获取个数
     * @return 留言列表
     * @author 郭欣光
     */
    public List<LeaveMessage> getLeaveMessageByLimitOrderByCreateTime(int start, int length);

    /**
     * 蝴蝶指定id的留言个数
     * @param id 留言ID
     * @return 留言个数
     * @author 郭欣光
     */
    public int getCountById(String id);

    /**
     * 创建留言信息
     * @param leaveMessage 留言信息
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int createLeaveMessage(LeaveMessage leaveMessage);

    /**
     * 删除留言
     * @param id 留言ID
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int deleteLeaveMessage(String id);
}

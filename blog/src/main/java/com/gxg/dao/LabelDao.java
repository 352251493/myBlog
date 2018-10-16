package com.gxg.dao;

import com.gxg.entities.Label;

import java.util.List;

/**
 * Label与数据库交互接口
 * @author 郭欣光
 * @date 2018/10/16 10:49
 */
public interface LabelDao {

    /**
     * 获取Label数量
     * @return 标签数量
     * @author 郭欣光
     */
    public int getCount();

    /**
     * 获取指定范围Label，按照time降序排列
     * @param start 开始坐标
     * @param length 获取个数
     * @return Label列表
     * @author 郭欣光
     */
    public List<Label> getLabelByLimitOrderByTime(int start, int length);

    /**
     * 获得指定id的标签个数
     * @param id 标签id
     * @return 标签个数
     * @author 郭欣光
     */
    public int getCountById(String id);

    /**
     * 添加标签
     * @param label 要添加的标签信息
     * @return 数据库改变的行数
     * @author 郭欣光
     */
    public int addLabel(Label label);

    /**
     * 删除指定id的标签
     * @param id 要删除的标签id
     * @return 数据库改变行数
     */
    public int deleteLabelById(String id);

    /**
     * 获取指定范围Label，按照time升序排列
     * @param start 开始坐标
     * @param length 要获得的个数
     * @return 指定范围的label
     * @author 郭欣光
     */
    public List<Label> getLabelByLimitOrderByTimeAsc(int start, int length);
}

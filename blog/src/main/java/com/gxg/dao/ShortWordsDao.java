package com.gxg.dao;

import com.gxg.entities.ShortWords;

import java.util.List;

/**
 * 毒鸡汤数据库交互接口
 * @author 郭欣光
 * @date 2018/10/16 15:39
 */
public interface ShortWordsDao {

    /**
     * 获取毒鸡汤个数
     * @return 毒鸡汤个数
     * @author 郭欣光
     */
    public int getCount();

    /**
     * 获取指定开始位置及指定个数毒鸡汤信息
     * @param start 指定的开始位置
     * @param length 指定获取个数
     * @return 毒鸡汤信息
     * @author 郭欣光
     */
    public List<ShortWords> getShortWordsByLimitOrderByTime(int start, int length);
}

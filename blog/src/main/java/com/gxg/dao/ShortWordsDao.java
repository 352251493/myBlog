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

    /**
     * 获取指定id的毒鸡汤个数
     * @param id 毒鸡汤id
     * @return int 毒鸡汤个数
     * @author 郭欣光
     */
    public int getCountById(String id);

    /**
     * 添加毒鸡汤
     * @param shortWords 毒鸡汤相关信息
     * @return 数据库改变个数
     * @author 郭欣光
     */
    public int addShortWords(ShortWords shortWords);

    /**
     * 获取按升序排序的指定位置的指定个数的毒鸡汤信息
     * @param start 指定开始位置
     * @param length 获取个数
     * @return 鸡汤信息
     * @author 郭欣光
     */
    public List<ShortWords> getShortWordsByLimitOrderByTimeAsc(int start, int length);

    /**
     * 根据毒鸡汤id删除相应的毒鸡汤
     * @param id 毒鸡汤id
     * @return 数据库改变行数
     * @author 郭欣光
     */
    public int deleteShortWordsById(String id);
}

package com.gxg.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 郭欣光
 * @date 2018/11/12 14:49
 */
public class TimeUtil {

    /**
     * 从Timestrap类型中提取年
     * @param time Timestrap
     * @return 年份
     * @author 郭欣光
     */
    public static String getYear(Timestamp time) {
        Date date = new Date(time.getTime());
        return getYear(date);
    }

    /**
     * 从Date类型中获取年
     * @param date Date类型
     * @return 年份
     * @author 郭欣光
     */
    public static String getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getYear(calendar);
    }

    /**
     * 从Calendar类型中获取年
     * @param calendar Calendar
     * @return 年份
     * @author 郭欣光
     */
    public static  String getYear(Calendar calendar) {
        return calendar.get(Calendar.YEAR) + "";
    }

    /**
     * 从Timestrap类型中获取月份
     * @param timestamp Timestrap类型
     * @return 月份
     * @author 郭欣光
     */
    public static String getMonth(Timestamp timestamp) {
        Date date = new Date(timestamp.getTime());
        return getMonth(date);
    }

    /**
     * 从Date类型中获取月份
     * @param date Date类型
     * @return 月份
     * @author 郭欣光
     */
    public static String getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return getMonth(calendar);
    }

    /**
     * 从Calendar类型中获取月份
     * @param calendar Calendar类型
     * @return 月份
     * @author 郭欣光
     */
    public static String getMonth(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH) + 1;
        return month + "";
    }
}

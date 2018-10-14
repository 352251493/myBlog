package com.gxg.utils;

import java.util.Random;

/**
 * 数字的工具类
 * @author 郭欣光
 * @date 2018/10/12 15:43
 */
public class NumberUtil {

    /**
     * 随机生成一个bitNumber位数，位数不足的前方补0
     * @param bitNumber 生成的位数
     * @return 生成的随机数，String类型
     * @author 郭欣光
     */
    public static String makeNumber(int bitNumber) {
        String baseRandomString = "";
        for (int i = 0; i < bitNumber; i++) {
            baseRandomString += "9";
        }
        int baseRandom = Integer.parseInt(baseRandomString);
        Random r = new Random();
        String num = r.nextInt(baseRandom) + "";
        StringBuffer stringBuffer = new StringBuffer();
        for(int i = 0; i < 4 - num.length(); i++) {
            stringBuffer.append("0");
        }
        num = stringBuffer.toString() + num;
        return num;
    }

    /**
     * 生成一个小于特定值的随机非负整数
     * @param baseRandom 应当小于的值
     * @return 随机的非负整数
     * @author 郭欣光
     */
    public static int randomInt(int baseRandom) {
        Random r = new Random();
        return r.nextInt(baseRandom);
    }
}

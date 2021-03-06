package com.gxg.utils;

import java.security.MessageDigest;

/**
 * Md5加密的工具类
 * @author 郭欣光
 * @date 2018/10/12 17:46
 */
public class Md5 {

    /**
     * md5加密算法
     * @param s 要加密的字符串
     * @return MD5加密后的字符串
     * @author 郭欣光
     */
    public static String md5 (String s) {
        char hexDigits[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        try {
            byte[] strTemp = s.getBytes();
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for(int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }catch(Exception e) {
            return null;
        }
    }
}

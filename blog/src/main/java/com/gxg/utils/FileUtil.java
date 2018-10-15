package com.gxg.utils;

import java.io.File;

/**
 * 文件处理工具类
 * @author 郭欣光
 * @date 2018/10/15 15:40
 */
public class FileUtil {

    public static boolean fileExists(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}

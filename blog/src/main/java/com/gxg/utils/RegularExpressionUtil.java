package com.gxg.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 提供常用的正则表达式验证
 * @author 郭欣光
 * @date 2018/10/19 15:30
 */
public class RegularExpressionUtil {

    /**
     * 验证url格式是否正确
     * @param string 要验证的字符串
     * @return 是否正确
     * @author 郭欣光
     */
    public static boolean checkUrl(String string) {
        String reg = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(string);
        return m.matches();
    }

    /**
     * 验证Email格式是否正确
     * @param string 要验证的字符串
     * @return 是否正确
     * @author 郭欣光
     */
    public static boolean checkEmail(String string) {
        String reg = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(reg);
        Matcher m = p.matcher(string);
        return m.matches();
    }
}

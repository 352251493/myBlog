package com.gxg.service;

import org.json.JSONObject;

/**
 * 邮件先关处理
 * @author 郭欣光
 * @date 2018/11/2 10:27
 */
public interface MailService {

    /**
     * 发送html邮件
     * @param to 收件人邮箱
     * @param subject 邮件主题
     * @param emailContent 邮件内容
     * @return 发送结果
     * @author 郭欣光
     */
    public JSONObject sendHtmlMail(String to, String subject, String emailContent);
}

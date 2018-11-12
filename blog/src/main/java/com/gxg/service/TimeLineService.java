package com.gxg.service;

import org.json.JSONObject;

/**
 * 时间轴相关业务处理
 * @author 郭欣光
 * @date 2018/11/12 11:24
 */
public interface TimeLineService {

    /**
     * 获取时间轴页面要显示的文章信息
     * @param pageNumber 页数
     * @return 页面显示相关信息
     * @author 郭欣光
     */
    public JSONObject getTimeLine(String pageNumber);
}

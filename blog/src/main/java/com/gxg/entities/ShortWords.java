package com.gxg.entities;

import java.sql.Timestamp;

/**
 * 毒鸡汤信息
 * @author 郭欣光
 * @date 2018/10/16 15:35
 */
public class ShortWords {

    private String id;

    private String words;

    private Timestamp time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}

package com.gxg.entities;

import java.sql.Timestamp;

/**
 * 毒鸡汤信息
 * @author 郭欣光
 * @date 2018/10/16 15:35
 */
public class ShortWords {

    private String id;

    private String word;

    private Timestamp time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }
}

package com.gxg.entities;

import java.sql.Timestamp;

/**
 * 个人博客标签
 * @author 郭欣光
 * @date 2018/10/16 10:34
 */
public class Label {

    private String id;

    private String name;

    private Timestamp time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

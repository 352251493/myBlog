package com.gxg.entities;

import java.sql.Timestamp;

/**
 * 图片美景相关信息
 * @author 郭欣光
 * @date 2018/11/8 14:45
 */
public class Image {

    private String id;

    private String title;

    private String img;

    private Timestamp uploadTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Timestamp getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Timestamp uploadTime) {
        this.uploadTime = uploadTime;
    }
}

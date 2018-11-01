package com.gxg.entities;

import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * 文章评论信息
 * @author 郭欣光
 * @date 2018/11/1 10:52
 */
public class ArticleComment {

    private String id;

    private String headImg;

    private String comment;

    private String name;

    private String email;

    private Timestamp createTime;

    private String articleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("id", id);
        jsonObject.accumulate("headImg", headImg);
        jsonObject.accumulate("comment", comment);
        jsonObject.accumulate("name", name);
        jsonObject.accumulate("email", email);
        jsonObject.accumulate("articleId", articleId);
        jsonObject.accumulate("createTime", createTime);
        return jsonObject.toString();
    }
}

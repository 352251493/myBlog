package com.gxg.entities;

import org.json.JSONObject;

/**
 * @author 郭欣光
 * @date 2018/10/12 17:10
 */
public class User {

    private String id;
    private String password;
    private String role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("id", id);
        jsonObject.accumulate("password", password);
        jsonObject.accumulate("role", role);
        return jsonObject.toString();
    }
}

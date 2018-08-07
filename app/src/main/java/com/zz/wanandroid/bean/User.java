package com.zz.wanandroid.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaozhuo
 * @date 2018/7/24 16:47
 */
public class User {


    /**
     * collectIds : [3187,3184,3182]
     * email :
     * icon :
     * id : 7831
     * password : 123456
     * type : 0
     * username : wanandroid2019
     */

    private String email;
    private String icon;
    private int id;
    private String password;
    private int type;
    private String username;
    private List<Integer> collectIds;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Integer> getCollectIds() {
        if(collectIds==null){
            collectIds= new ArrayList<>();
        }
        return collectIds;
    }

    public void setCollectIds(List<Integer> collectIds) {
        this.collectIds = collectIds;
    }
}

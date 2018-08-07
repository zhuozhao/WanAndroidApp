package com.zz.wanandroid.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author zhaozhuo
 * @date 2018/7/19 9:29
 */
@Entity
public class SearchHistory {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private  int userId;
    private String key;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

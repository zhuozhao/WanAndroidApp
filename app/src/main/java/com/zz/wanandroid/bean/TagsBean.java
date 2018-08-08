package com.zz.wanandroid.bean;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * @author zhaozhuo
 * @date 2018/8/8 16:03
 */
@Entity
public class TagsBean {

    /**
     * name : 项目
     * url : /project/list/1?cid=382
     */

    @PrimaryKey(autoGenerate = true)
    private  int id;
    private int articleId;
    private String name;
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }
}

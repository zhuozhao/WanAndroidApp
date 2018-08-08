package com.zz.wanandroid.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.zz.wanandroid.bean.Article;

import java.util.List;

/**
 * @author zhaozhuo
 * @date 2018/8/8 15:27
 */
@Dao
public interface ArticleDao {


    @Query("select * from article")
    List<Article> getArticles();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long [] insertOrUpdateBanners(List<Article> banners);
}

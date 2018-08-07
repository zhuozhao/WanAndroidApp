package com.zz.wanandroid.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


import com.zz.wanandroid.bean.Banner;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

/**
 * @author zhaozhuo
 * @date 2018/7/6 14:16
 */
@Dao
public interface BannerDao {

    /**
     * 查询banner数据
     *
     * @return
     */

    @Query("select * from banner ORDER BY id ASC")
    List<Banner> getBanners();

    /**
     * 更新/插入banner数据
     *
     * @param banners
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long [] insertOrUpdateBanners(List<Banner> banners);

}

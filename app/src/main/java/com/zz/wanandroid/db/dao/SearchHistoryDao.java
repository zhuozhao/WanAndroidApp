package com.zz.wanandroid.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.zz.wanandroid.bean.SearchHistory;

import java.util.List;

import io.reactivex.Flowable;

/**
 * @author zhaozhuo
 * @date 2018/7/19 9:38
 */
@Dao
public interface SearchHistoryDao {


    @Query("select distinct `key` ,* from searchhistory where userId = :uid ")
    Flowable<List<SearchHistory>> getSearchHistory(int uid);

    @Insert
    void insertHistory(SearchHistory history);

    @Delete()
    void deleteHistories(List<SearchHistory> histories);

}

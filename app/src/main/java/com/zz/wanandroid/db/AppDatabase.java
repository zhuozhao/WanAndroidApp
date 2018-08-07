package com.zz.wanandroid.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zz.wanandroid.app.MyApplication;
import com.zz.wanandroid.bean.Banner;
import com.zz.wanandroid.bean.SearchHistory;
import com.zz.wanandroid.db.dao.BannerDao;
import com.zz.wanandroid.db.dao.SearchHistoryDao;

/**
 * @author zhaozhuo
 * @date 2018/7/5 15:10
 */

@Database( entities =  {Banner.class, SearchHistory.class},version = 2)
public abstract class AppDatabase extends RoomDatabase{

    private static final String TAG = "AppDatabase";
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance() {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(MyApplication.getINSTANCE(),
                            AppDatabase.class, "wanandroid.db")
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    //  数据库创建回调；
                                    super.onCreate(db);
                                    Log.d(TAG, "onCreate: ");
                                }

                                @Override
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    //  数据库使用回调；
                                    super.onOpen(db);
                                    Log.d(TAG, "onOpen: ");
                                }
                            })
                            .allowMainThreadQueries()   // 数据库操作可运行在主线程
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    /**
     *
     * @return
     */
    public abstract BannerDao bannerDao();

    /**
     * 搜索历史
     * @return
     */
    public abstract SearchHistoryDao searchHistoryDao();


}

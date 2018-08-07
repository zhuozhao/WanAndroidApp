package com.zz.wanandroid.app;

import android.app.Application;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.bumptech.glide.util.LogTime;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.zz.wanandroid.R;
import com.zz.wanandroid.bean.Result;
import com.zz.wanandroid.bean.User;
import com.zz.wanandroid.db.AppDatabase;

/**
 * @author zhaozhuo
 * @date 2018/7/10 13:57
 */
public class MyApplication extends Application {
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                //全局设置主题颜色
                layout.setPrimaryColorsId( R.color.windowBackground,R.color.colorPrimary);
                //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
                return new ClassicsHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
        //设置主题

       // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }
    private static final String TAG = "MyApplication";
    private static MyApplication INSTANCE;
    private User user;
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        initDB();
        initUser();
    }
    private void initUser(){

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);

        String userJson = sharedPreferences.getString("userJson",null);
        if(userJson!=null){
            setUser(new Gson().fromJson(userJson,User.class));
        }
    }

    public static  MyApplication getINSTANCE(){

        return INSTANCE;
    }
    /**
     * 初始化room
     */
    private void initDB(){

        AppDatabase mDatabase = Room.databaseBuilder(this,
                AppDatabase.class, "wanandroid.db")
                .build();

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

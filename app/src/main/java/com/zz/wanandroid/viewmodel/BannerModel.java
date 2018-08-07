package com.zz.wanandroid.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.zz.wanandroid.app.MyApplication;
import com.zz.wanandroid.bean.Banner;
import com.zz.wanandroid.bean.Result;
import com.zz.wanandroid.bean.BaseObserver;
import com.zz.wanandroid.db.AppDatabase;
import com.zz.wanandroid.db.dao.BannerDao;
import com.zz.wanandroid.http.WanAndroidService;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhaozhuo
 * @date 2018/7/4 10:38
 */
public class BannerModel extends ViewModel {

    private static final String TAG = "BannerModel";

    private MutableLiveData<List<Banner>> listMutableLiveData;

    public MutableLiveData<List<Banner>> getListMutableLiveData() {

        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }


    /**
     * 更新数据
     */
    private void updateDate() {

        WanAndroidService.getWanAndroidApi().banner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<Banner>>() {
                    @Override
                    protected void onFailure(Throwable e) {
                        listMutableLiveData.postValue(null);
                    }

                    @Override
                    protected void onSuccees(final Result<List<Banner>> result) {

                        listMutableLiveData.postValue(result.getData());
                        //AppDatabase.getInstance().bannerDao().insertOrUpdateBanners(result.getData());
                    }
                });

    }

    public void getBanners() {

       // List<Banner> banners = AppDatabase.getInstance().bannerDao().getBanners();

//        if(banners!=null){
//            listMutableLiveData.postValue(banners);
//        }
        updateDate();
    }

}

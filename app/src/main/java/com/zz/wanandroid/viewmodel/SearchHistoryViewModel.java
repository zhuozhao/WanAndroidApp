package com.zz.wanandroid.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zz.wanandroid.bean.Banner;
import com.zz.wanandroid.bean.BaseObserver;
import com.zz.wanandroid.bean.Result;
import com.zz.wanandroid.bean.SearchHistory;
import com.zz.wanandroid.db.AppDatabase;
import com.zz.wanandroid.http.WanAndroidService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhaozhuo
 * @date 2018/7/4 10:38
 */
public class SearchHistoryViewModel extends ViewModel {

    private static final String TAG = "BannerModel";

    private MutableLiveData<List<SearchHistory>> listMutableLiveData;

    public MutableLiveData<List<SearchHistory>> getListMutableLiveData() {

        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }

    /**
     * 插入历史记录
     * @param history
     */
    public void insertHistory( SearchHistory history) {

        AppDatabase.getInstance().searchHistoryDao().insertHistory(history);

    }

    /**
     * 获取搜搜历史记录
     * @param uId
     */
    public void getHistories(int uId) {

        AppDatabase.getInstance().searchHistoryDao().getSearchHistory(uId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<SearchHistory>>() {
                    @Override
                    public void accept(List<SearchHistory> histories) throws Exception {
                        listMutableLiveData.postValue(histories);
                    }
                });
    }

    public void deleteHistories(List<SearchHistory> histories){

        AppDatabase.getInstance().searchHistoryDao().deleteHistories(histories);

    }

}

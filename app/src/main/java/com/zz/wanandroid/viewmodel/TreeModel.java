package com.zz.wanandroid.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zz.wanandroid.bean.BaseObserver;
import com.zz.wanandroid.bean.Result;
import com.zz.wanandroid.bean.Tree;
import com.zz.wanandroid.http.WanAndroidService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhaozhuo
 * @date 2018/7/16 15:34
 */
public class TreeModel extends ViewModel{

    private MutableLiveData<List<Tree>> listMutableLiveData;

    public MutableLiveData<List<Tree>> getListMutableLiveData() {

        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }

    /**
     * 更新数据
     */
    public void updateDate() {

        WanAndroidService.getWanAndroidApi().tree()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<Tree>>() {
                    @Override
                    protected void onFailure(Throwable e) {
                        listMutableLiveData.postValue(null);
                    }

                    @Override
                    protected void onSuccees(Result<List<Tree>> result) {
                        listMutableLiveData.postValue(result.getData());
                    }
                });

    }
}

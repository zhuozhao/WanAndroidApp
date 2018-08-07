package com.zz.wanandroid.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zz.wanandroid.bean.BaseObserver;
import com.zz.wanandroid.bean.Hotkey;
import com.zz.wanandroid.bean.Result;
import com.zz.wanandroid.bean.Website;
import com.zz.wanandroid.http.WanAndroidService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhaozhuo
 * @date 2018/7/18 14:47
 */
public class FriendViewModel extends ViewModel {

    private MutableLiveData<List<Website>> listMutableLiveData;

    public MutableLiveData<List<Website>> getListMutableLiveData() {

        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }

    /**
     * 更新数据
     */
    public void getFriendsWebsite() {

        WanAndroidService.getWanAndroidApi().friend()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<Website>>() {
                    @Override
                    protected void onFailure(Throwable e) {
                        listMutableLiveData.postValue(null);
                    }

                    @Override
                    protected void onSuccees(final Result<List<Website>> result) {

                        listMutableLiveData.postValue(result.getData());
                    }
                });

    }
}

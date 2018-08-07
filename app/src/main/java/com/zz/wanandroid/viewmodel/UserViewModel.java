package com.zz.wanandroid.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.zz.wanandroid.bean.Banner;
import com.zz.wanandroid.bean.BaseObserver;
import com.zz.wanandroid.bean.Result;
import com.zz.wanandroid.bean.User;
import com.zz.wanandroid.http.WanAndroidService;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * @author zhaozhuo
 * @date 2018/7/24 10:29
 */
public class UserViewModel extends ViewModel {

    private static final String TAG = "UserViewModel";
    private MutableLiveData<Result<User>> listMutableLiveData;

    public MutableLiveData<Result<User>> getMutableLiveData() {

        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
        }
        return listMutableLiveData;
    }

    /**
     * 登录
     * @param username
     * @param password
     */
    public void login(String username,String password){

        WanAndroidService.getWanAndroidApi().login(username,password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<User> userResult) {
                        listMutableLiveData.setValue(userResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listMutableLiveData.postValue(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 注册
     * @param username
     * @param password
     * @param repassword
     */
    public void register(String username,String password,String repassword){

        WanAndroidService.getWanAndroidApi().register(username,password,repassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result<User>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result<User> userResult) {
                        listMutableLiveData.postValue(userResult);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listMutableLiveData.postValue(null);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

}

package com.zz.wanandroid.bean;



import com.zz.wanandroid.bean.Result;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * @author zhaozhuo
 * @date 2018/7/6 9:03
 */
public abstract class BaseObserver<T> implements Observer<Result<T>> {


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Result<T> tResult) {

        onSuccees(tResult);
    }

    @Override
    public void onError(Throwable e) {

        onFailure(e);
        e.printStackTrace();

    }

    @Override
    public void onComplete() {

    }

    /**
     * 失败返回
     * @param e
     * @throws Exception
     */

    protected abstract void onFailure(Throwable e) ;

    /**
     * 成功返回
     * @param result
     * @throws Exception
     */

    protected abstract void onSuccees(Result<T> result) ;

}

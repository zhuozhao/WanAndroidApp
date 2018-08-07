package com.zz.wanandroid.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.zz.wanandroid.bean.Article;
import com.zz.wanandroid.bean.BaseObserver;
import com.zz.wanandroid.bean.PageData;
import com.zz.wanandroid.bean.Result;
import com.zz.wanandroid.http.WanAndroidService;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author zhaozhuo
 * @date 2018/7/17 13:55
 */
public class TreeArticleViewModel extends ViewModel {

    private MutableLiveData<PageData<Article>> pageDataMutableLiveData;


    public MutableLiveData<PageData<Article>> getPageDataMutableLiveData() {


        if (pageDataMutableLiveData == null) {
            pageDataMutableLiveData = new MutableLiveData<>();
        }

        return pageDataMutableLiveData;
    }

    /**
     * 获取体系下的文章
     * @param pageNum
     * @param cid
     */
    public void getTreeArticle(int pageNum, int cid) {

        WanAndroidService.getWanAndroidApi().articleListTree(pageNum, cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<PageData<Article>>() {
                    @Override
                    protected void onFailure(Throwable e) {
                        pageDataMutableLiveData.postValue(null);
                    }

                    @Override
                    protected void onSuccees(Result<PageData<Article>> result) {

                        pageDataMutableLiveData.postValue(result.getData());
                    }
                });

    }


}

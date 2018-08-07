package com.zz.wanandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zz.wanandroid.app.MyApplication;
import com.zz.wanandroid.bean.Article;
import com.zz.wanandroid.bean.Result;
import com.zz.wanandroid.http.WanAndroidService;
import com.zz.wanandroid.http.api.WanAndroidApi;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ArticleInfoActivity extends AppCompatActivity {

    private static final String TAG = "ArticleInfoActivity";
    private WebView myWebView;
    private ProgressBar progressBar;
    private int id;
    private MenuItem collectMenu;
    private Article article;
    //0代表是其他地方，1代表是收藏列表
    private int type =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_info);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        myWebView = findViewById(R.id.myWebView);
        progressBar = findViewById(R.id.progressBar);
        article = (Article) getIntent().getSerializableExtra("article");
        type = getIntent().getIntExtra("type",0);
        if(article!=null){
            toolbar.setTitle(article.getTitle());
            myWebView.loadUrl(article.getLink());
            id = article.getId();
        }

        //网页在app内打开
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        myWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //加载完网页进度条消失
                    progressBar.setVisibility(View.GONE);
                } else {
                    //开始加载网页时显示进度条
                    progressBar.setVisibility(View.VISIBLE);
                    //设置进度值
                    progressBar.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

  //    List<Integer> collectIds =  MyApplication.getINSTANCE().getUser().getCollectIds();

//      if (collectIds.contains(article.getId())){
//          article.setCollect(true);
//      }else {
//          article.setCollect(false);
//      }

        //1代表是从我的收藏列表点击
        if(type==1){
            article.setCollect(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.article_info_menu, menu);
        collectMenu = menu.findItem(R.id.collect);
        if(article!=null){
            if(article.isCollect()){
                collectMenu.setIcon(R.drawable.ic_favorite_black_24dp);
            }else {
                collectMenu.setIcon(R.drawable.ic_favorite_border_black_24dp);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.collect:
                if(article.isCollect()){
                    if(type==0){
                        unCollect();
                    }else {
                        unCollect2();
                    }
                }else {
                    collectArticle();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void collectArticle(){

        WanAndroidService.getWanAndroidApi().collect(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        //{"errorCode":-1,"errorMsg":"请先登录！"}
                        //{"data":null,"errorCode":0,"errorMsg":""}
                        Log.d(TAG, "onNext: "+result);
                        if (result.getErrorCode()==0){
                            article.setCollect(true);
                            collectMenu.setIcon(R.drawable.ic_favorite_black_24dp);
                            MyApplication.getINSTANCE().getUser().getCollectIds().add(id);
                            Toast.makeText(ArticleInfoActivity.this,"收藏成功",Toast.LENGTH_SHORT).show();
                        }

                        if (result.getErrorCode()==-1){

                            Toast.makeText(ArticleInfoActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(ArticleInfoActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG, "onError: ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {


                    }
                });
    }


    private void unCollect(){
        WanAndroidService.getWanAndroidApi().unCollectOriginId(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        //{"errorCode":-1,"errorMsg":"请先登录！"}
                        //{"data":null,"errorCode":0,"errorMsg":""}
                        Log.d(TAG, "onNext: "+result);
                        if (result.getErrorCode()==0){
                            article.setCollect(false);
                            collectMenu.setIcon(R.drawable.ic_favorite_border_black_24dp);
                            MyApplication.getINSTANCE().getUser().getCollectIds().remove((Integer)id);
                            Toast.makeText(ArticleInfoActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                        }

                        if (result.getErrorCode()==-1){

                            Toast.makeText(ArticleInfoActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(ArticleInfoActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG, "onError: ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {


                    }
                });


    }

    private void unCollect2(){
        WanAndroidService.getWanAndroidApi().unCollect(id,article.getOriginId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Result result) {
                        //{"errorCode":-1,"errorMsg":"请先登录！"}
                        //{"data":null,"errorCode":0,"errorMsg":""}
                        Log.d(TAG, "onNext: "+result);
                        if (result.getErrorCode()==0){
                            article.setCollect(false);
                            collectMenu.setIcon(R.drawable.ic_favorite_border_black_24dp);
                            MyApplication.getINSTANCE().getUser().getCollectIds().remove((Integer)id);
                            Toast.makeText(ArticleInfoActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                        }

                        if (result.getErrorCode()==-1){

                            Toast.makeText(ArticleInfoActivity.this,"请先登录",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(ArticleInfoActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                        Log.d(TAG, "onError: ");
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {


                    }
                });


    }

}
